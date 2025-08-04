package com.vvwxx.learn.donation.service

import com.vvwxx.learn.donation.dto.response.BaseResponse
import com.vvwxx.learn.donation.dto.request.DonationRequest
import com.vvwxx.learn.donation.dto.response.DonationResponse
import com.vvwxx.learn.donation.dto.response.MidtransNotifRes
import org.springframework.http.ResponseEntity

interface DonationService {

    fun createDonationTransaction(request: DonationRequest): BaseResponse<DonationResponse>

    fun handleNotification(data: MidtransNotifRes) : ResponseEntity<String>
}