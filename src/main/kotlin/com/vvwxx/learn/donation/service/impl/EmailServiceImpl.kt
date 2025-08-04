package com.vvwxx.learn.donation.service.impl

import com.vvwxx.learn.donation.dto.EmailDetails
import com.vvwxx.learn.donation.entity.DonationEntity
import com.vvwxx.learn.donation.service.EmailService
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl(
    private val javaMailSender: JavaMailSender,
    @Value("\${spring.mail.username}") private val sender: String,
) : EmailService {

    // send simple email without attachment
    override fun sendSimpleEmail(details: EmailDetails): String {

        try {

            val mailMessage = SimpleMailMessage()

            // setup email detail
            mailMessage.from = sender
            mailMessage.setTo(details.recipient)
            mailMessage.text = details.messageBody
            mailMessage.subject = details.subject

            // sending email
            javaMailSender.send(mailMessage)
            return "success"

        } catch (e: Exception) {
            return "Error while Sending Mail : ${e.message}"
        }
    }

    override fun sendingEmailToDonator(donation: DonationEntity, messageBody: String) {

        val email = EmailDetails(
            recipient = donation.email ?: "",
            messageBody = messageBody,
            subject = "Donation Team Payment"
        )

        val res = sendSimpleEmail(email)

        print("Email sent message: $res")
    }
}