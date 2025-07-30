package com.vvwxx.learn.donation.model

data class DonationResponse(

    var bank: String? = null,
    var amount: String? = null,
    var vaNumber: String? = null,
    var linkPayment: String? = null,
)