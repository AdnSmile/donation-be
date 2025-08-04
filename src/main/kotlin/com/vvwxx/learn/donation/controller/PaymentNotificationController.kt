package com.vvwxx.learn.donation.controller

import com.vvwxx.learn.donation.dto.response.MidtransNotifRes
import com.vvwxx.learn.donation.dto.response.VaNumbers
import com.vvwxx.learn.donation.service.DonationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payment")
class PaymentNotificationController(
    private val donationService: DonationService
) {

    @PostMapping("/notification")
    fun handleNotification(@RequestBody payload: Map<String, Object>) : ResponseEntity<String> {

        val res = MidtransNotifRes()
        val vaNumber = VaNumbers()
        val vaList = payload["va_numbers"] as List<Map<String, String>>

        vaNumber.apply {
            this.vaNumber = vaList[0]["va_number"]
            bank = vaList[0]["bank"]
        }

        res.apply {
            vaNumbers = listOf(vaNumber)
            transactionTime = payload["transaction_time"] as? String
            transactionStatus = payload["transaction_status"] as? String
            transactionId = payload["transaction_id"] as? String
            statusMessage = payload["status_message"] as? String
            statusCode = payload["status_code"] as? String
            signatureKey = payload["signature_key"] as? String
            paymentType = payload["payment_type"] as? String
            orderId = payload["order_id"] as? String
            merchantId = payload["merchant_id"] as? String
            grossAmount = payload["gross_amount"] as? String
            fraudStatus = payload["fraud_status"] as? String
            expiryTime = payload["expiry_time"] as? String
            currency = payload["currency"] as? String
        }

        return donationService.handleNotification(res)
    }
}