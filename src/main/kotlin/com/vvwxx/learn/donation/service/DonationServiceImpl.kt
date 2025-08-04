package com.vvwxx.learn.donation.service

import com.vvwxx.learn.donation.model.BankTransfer
import com.vvwxx.learn.donation.model.BaseResponse
import com.vvwxx.learn.donation.model.CustomerDetails
import com.vvwxx.learn.donation.model.DonationEntity
import com.vvwxx.learn.donation.model.DonationRequest
import com.vvwxx.learn.donation.model.DonationResponse
import com.vvwxx.learn.donation.model.MidtransNotifRes
import com.vvwxx.learn.donation.model.MidtransRequest
import com.vvwxx.learn.donation.model.TransactionDetails
import com.vvwxx.learn.donation.repository.DonationRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.Date
import java.util.UUID

@Service
class DonationServiceImpl(
    @Value("\${MIDTRANS_URL}") private val midtransUrl: String,
    @Value("\${SERVER_KEY}") private val serverKey: String,
    private val donationRepository: DonationRepository,
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

        donationData.apply {
            this.amount = request.amount
            this.name = request.name
            this.email = request.email
            this.note = request.note
            this.paymentType = "bank_transfer"
            this.bank = request.bank?.lowercase()
            this.paymentStatus = "pending"
        }

        headers.contentType = MediaType.APPLICATION_JSON
        headers.setBasicAuth(serverKey, "")

        val entity = HttpEntity(midtransReq, headers)

        val response: ResponseEntity<Map<*, *>> = restTamplet.postForEntity(
            midtransUrl,
            entity,
            Map::class.java
        )

        val responseBody: Map<*, *>? = response.body

        if (responseBody != null && responseBody.containsKey("va_numbers")) {
            val vaList = responseBody["va_numbers"] as List<Map<String, String>>
            val va = vaList[0]["va_number"] ?: ""
            donationRepository.save(donationData)
            return BaseResponse(
                status = "Success",
                message = "Donation transaction created successfully",
                data = DonationResponse(
                    bank = request.bank!!.uppercase(),
                    amount = request.amount.toString(),
                    vaNumber = va,
                    linkPayment = "https://simulator.sandbox.midtrans.com/${request.bank}/va/index"
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

        if (donationData.isPresent) {
            val donation = donationData.get()

            if (data.transactionStatus == "settlement") {
                donation.paymentDate = Date()
                donation.updatedAt = Date()
                donation.paymentStatus = data.transactionStatus
            }

            donationRepository.save(donation)

            TODO("Send email notification to donor")

        return ResponseEntity.ok("Notification processed successfully")
        }

        return ResponseEntity.status(404).body("Donation not found")
    }
}