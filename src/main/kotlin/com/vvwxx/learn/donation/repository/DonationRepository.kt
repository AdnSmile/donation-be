package com.vvwxx.learn.donation.repository

import com.vvwxx.learn.donation.model.DonationEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DonationRepository: JpaRepository<DonationEntity, UUID> {
}