package com.vvwxx.learn.donation.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "donation")
data class DonationEntity (

    @Id
    val id: UUID = UUID.randomUUID(),

    val amount: Long? = null,

    val name: String,

    val email: String,

    val note: String? = null,

    val paymentType: String = "payment_type",

    val bank: String? = null,
)