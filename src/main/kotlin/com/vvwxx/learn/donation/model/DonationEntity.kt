package com.vvwxx.learn.donation.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "donation")
data class DonationEntity (

    @Id
    val id: UUID = UUID.randomUUID(),

    var amount: Long? = null,

    var name: String? = null,

    var email: String? = null,

    var note: String? = null,

    var paymentType: String = "bank_transfer",

    var bank: String? = null,

    var paymentStatus: String? = null,

    var paymentDate: Date? = null,

    val createdAt: Date = Date(),

    var updatedAt: Date? = null,
)