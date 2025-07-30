package com.vvwxx.learn.donation.service

import com.vvwxx.learn.donation.model.BaseResponse
import com.vvwxx.learn.donation.model.DonationRequest
import com.vvwxx.learn.donation.model.DonationResponse

interface DonationService {

    fun createDonationTransaction(request: DonationRequest): BaseResponse<DonationResponse>
}