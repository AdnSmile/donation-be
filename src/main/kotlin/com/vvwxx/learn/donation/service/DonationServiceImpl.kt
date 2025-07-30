package com.vvwxx.learn.donation.service

import com.vvwxx.learn.donation.model.BankTransfer
import com.vvwxx.learn.donation.model.BaseResponse
import com.vvwxx.learn.donation.model.CustomerDetails
import com.vvwxx.learn.donation.model.DonationEntity
import com.vvwxx.learn.donation.model.DonationRequest
import com.vvwxx.learn.donation.model.DonationResponse
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
}