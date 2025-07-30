package com.vvwxx.learn.donation.model

import jakarta.validation.constraints.NotEmpty

data class DonationRequest(

    @field:NotEmpty(message = "Name is required")
    val name: String? = null,

    @field:NotEmpty(message = "Amount is required")
    val amount: Long? = null,

    @field:NotEmpty(message = "Email is required")
    val email: String? = null,

    val note: String? = null,

    @field:NotEmpty(message = "Bank is required")
    val bank: String? = null
)
