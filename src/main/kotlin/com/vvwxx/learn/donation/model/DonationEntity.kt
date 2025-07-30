package com.vvwxx.learn.donation.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "donation")
data class DonationEntity (

    @Id
    val id: UUID = UUID.randomUUID(),

    val amount: Long? = null,

    val name: String? = null,

    val email: String? = null,

    val note: String? = null,

    val paymentType: String = "bank_transfer",

    val bank: String? = null,

    val paymentStatus: String? = null,

    val paymentDate: Date? = null,

    val createdAt: Date = Date(),

    val updatedAt: Date? = null,
)