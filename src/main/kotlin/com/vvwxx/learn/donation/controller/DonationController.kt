package com.vvwxx.learn.donation.controller

import com.vvwxx.learn.donation.dto.response.BaseResponse
import com.vvwxx.learn.donation.dto.request.DonationRequest
import com.vvwxx.learn.donation.dto.response.DonationResponse
import com.vvwxx.learn.donation.service.DonationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/donate")
class DonationController(
    private val donationService: DonationService
) {

    @PostMapping
    fun createDonation(@RequestBody request: DonationRequest) : BaseResponse<DonationResponse> {

        return donationService.createDonationTransaction(request)
    }
}