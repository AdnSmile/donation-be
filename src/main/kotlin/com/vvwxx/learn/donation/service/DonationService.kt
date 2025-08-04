package com.vvwxx.learn.donation.service

import com.vvwxx.learn.donation.model.BaseResponse
import com.vvwxx.learn.donation.model.DonationRequest
import com.vvwxx.learn.donation.model.DonationResponse
import com.vvwxx.learn.donation.model.MidtransNotifRes
import org.springframework.http.ResponseEntity

interface DonationService {

    fun createDonationTransaction(request: DonationRequest): BaseResponse<DonationResponse>

    fun handleNotification(data: MidtransNotifRes) : ResponseEntity<String>
}