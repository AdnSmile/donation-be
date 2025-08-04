package com.vvwxx.learn.donation.dto.response

data class MidtransNotifRes(

    var vaNumbers: List<VaNumbers>? = null,

    var transactionTime: String? = null,

    var transactionStatus: String? = null,

    var transactionId: String? = null,

    var statusMessage: String? = null,

    var statusCode: String? = null,

    var signatureKey: String? = null,

    var paymentType: String? = null,

    var orderId: String? = null,

    var merchantId: String? = null,

    var grossAmount: String? = null,

    var fraudStatus: String? = null,

    var expiryTime: String? = null,

    var currency: String? = null
)

data class  VaNumbers(
    var vaNumber: String? = null,
    var bank: String? = null
)
