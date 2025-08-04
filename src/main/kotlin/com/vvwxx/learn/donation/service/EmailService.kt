package com.vvwxx.learn.donation.service

import com.vvwxx.learn.donation.dto.EmailDetails
import com.vvwxx.learn.donation.entity.DonationEntity

interface EmailService {

    fun sendSimpleEmail(details: EmailDetails) : String

    fun sendingEmailToDonator(donation: DonationEntity, messageBody: String)
}