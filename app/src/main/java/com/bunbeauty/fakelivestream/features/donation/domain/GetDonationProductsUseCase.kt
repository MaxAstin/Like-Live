package com.bunbeauty.fakelivestream.features.donation.domain

import com.bunbeauty.fakelivestream.features.billing.BillingService
import com.bunbeauty.fakelivestream.features.billing.Product
import javax.inject.Inject

private const val DONATION_PREFIX = "donation_"

class GetDonationProductsUseCase @Inject constructor(
    private val billingService: BillingService
) {

    suspend operator fun invoke(): List<Product> {
        val productIds = List(6) { i ->
            "$DONATION_PREFIX${i + 1}"
        }
        return billingService.getOneTypeProducts(ids = productIds).orEmpty()
    }

}