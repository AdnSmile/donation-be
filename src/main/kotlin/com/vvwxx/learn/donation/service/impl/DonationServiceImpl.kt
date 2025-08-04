package com.vvwxx.learn.donation.service.impl

import com.vvwxx.learn.donation.dto.request.BankTransfer
import com.vvwxx.learn.donation.dto.request.CustomerDetails
import com.vvwxx.learn.donation.dto.request.DonationRequest
import com.vvwxx.learn.donation.dto.request.MidtransRequest
import com.vvwxx.learn.donation.dto.request.TransactionDetails
import com.vvwxx.learn.donation.dto.response.BaseResponse
import com.vvwxx.learn.donation.dto.response.DonationResponse
import com.vvwxx.learn.donation.dto.response.MidtransNotifRes
import com.vvwxx.learn.donation.entity.DonationEntity
import com.vvwxx.learn.donation.repository.DonationRepository
import com.vvwxx.learn.donation.service.DonationService
import com.vvwxx.learn.donation.service.EmailService
import com.vvwxx.learn.donation.utils.Utils
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.Date
import java.util.UUID
import kotlin.collections.get

@Service
class DonationServiceImpl(
    @Value("\${MIDTRANS_URL}") private val midtransUrl: String,
    @Value("\${SERVER_KEY}") private val serverKey: String,
    private val donationRepository: DonationRepository,
    private val emailService: EmailService,
): DonationService {

    override fun createDonationTransaction(request: DonationRequest): BaseResponse<DonationResponse> {

        val restTamplet = RestTemplate()
        val headers = HttpHeaders()

        val donationData = DonationEntity()
        val orderId = "Donation-${donationData.id}"
        val midtransReq = MidtransRequest(
            paymentType = "bank_transfer",
            transactionDetails = TransactionDetails(
                orderId = orderId,
                grossAmount = request.amount
            ),
            bankTransfer = BankTransfer(bank = request.bank),
            customerDetails = CustomerDetails(
                firstName = request.name,
                email = request.email
            )
        )

        headers.contentType = MediaType.APPLICATION_JSON
        headers.setBasicAuth(serverKey, "")

        val entity = HttpEntity(midtransReq, headers)

        val response: ResponseEntity<Map<*, *>> = restTamplet.postForEntity(
            midtransUrl,
            entity,
            Map::class.java
        )

        val responseBody: Map<*, *>? = response.body
        val linkPayment = "https://simulator.sandbox.midtrans.com/${request.bank}/va/index"

        if (responseBody != null && responseBody.containsKey("va_numbers")) {
            val vaList = responseBody["va_numbers"] as List<Map<String, String>>
            val va = vaList[0]["va_number"] ?: ""

            donationData.apply {
                this.amount = request.amount
                this.name = request.name
                this.email = request.email
                this.note = request.note
                this.paymentType = "bank_transfer"
                this.bank = request.bank?.lowercase()
                this.paymentStatus = "pending"
                this.vaNumber = va
            }

            donationRepository.save(donationData)

            val rupiahFormat = Utils.formatRupiah(donationData.amount)
            val messageBody = "Dear ${donationData.name},\n\n" +
                    "Silakan lakukan pembayaran untuk menyelesaikan donasi anda\n\n" +
                    "Informasi Pembayaran:\n" +
                    "Bank: ${donationData.bank}\n" +
                    "Jumlah Donasi: $rupiahFormat\n" +
                    "Nomor Va: ${donationData.vaNumber}\n" +
                    "Link Payment: $linkPayment \n\n" +
                    "Your support is greatly appreciated!\n\n" +
                    "Best regards,\n" +
                    "The Donation Team"

            emailService.sendingEmailToDonator(donationData, messageBody)

            return BaseResponse(
                status = "Success",
                message = "Donation transaction created successfully",
                data = DonationResponse(
                    bank = request.bank!!.uppercase(),
                    amount = request.amount.toString(),
                    vaNumber = va,
                    linkPayment = linkPayment
                )
            )
        }

        return BaseResponse(
            status = "Failed",
            message = "Failed to create donation transaction",
            data = null
        )
    }

    override fun handleNotification(data: MidtransNotifRes): ResponseEntity<String> {

        val orderId: UUID = UUID.fromString(data.orderId?.substringAfter("Donation-"))
        val donationData = donationRepository.findById(orderId)
        var messageBody: String

        if (donationData.isPresent) {
            val donation = donationData.get()
            val rupiahFormat = Utils.formatRupiah(donation.amount)

            if (data.transactionStatus == "settlement") {
                donation.paymentDate = Date()
                donation.updatedAt = Date()
                donation.paymentStatus = data.transactionStatus

                messageBody = "Dear ${donation.name},\n\n" +
                        "Donasi anda sebanyak $rupiahFormat sudah berhasil!\n" +
                        "Your support is greatly appreciated!\n\n" +
                        "Best regards,\n" +
                        "The Donation Team"

                donationRepository.save(donation)

                emailService.sendingEmailToDonator(donationData.get(), messageBody)
            }

            return ResponseEntity.ok("Notification processed successfully")
        }

        return ResponseEntity.status(404).body("Donation not found")
    }


}