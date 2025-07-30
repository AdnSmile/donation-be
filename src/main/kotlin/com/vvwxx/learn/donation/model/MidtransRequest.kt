package com.vvwxx.learn.donation.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.util.*

data class MidtransRequest(

    @field:JsonProperty("payment_type")
    var paymentType: String? = null,

    @field:JsonProperty("transaction_details")
    var transactionDetails: TransactionDetails? = null,

    @field:JsonProperty("bank_transfer")
    var bankTransfer: BankTransfer? = null,

    @field:JsonProperty("customer_details")
    var customerDetails: CustomerDetails? = null,
)

data class CustomerDetails(

    @field:JsonProperty("first_name")
    var firstName: String? = null,

    var email: String? = null,
)

data class TransactionDetails(

    @field:JsonProperty("order_id")
    var orderId: String? = null,

    @field:JsonProperty("gross_amount")
    var grossAmount: Long? = null,
)

data class BankTransfer(

    var bank: String? = null,
)