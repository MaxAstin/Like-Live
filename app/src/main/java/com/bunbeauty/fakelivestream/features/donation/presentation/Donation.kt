package com.bunbeauty.fakelivestream.features.donation.presentation

import com.bunbeauty.fakelivestream.common.presentation.Base
import com.bunbeauty.fakelivestream.features.billing.Product
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

interface Donation {

    data class State(
        val productList: ImmutableList<Product> = persistentListOf(),
        val shownProduct: Product? = null,
        val shownSuccessDonation: Product? = null,
    ) : Base.State

    sealed interface Action : Base.Action {
        data class DonationClick(val productId: String) : Action
        data object HideDonation : Action
        data class DonateClick(val productId: String) : Action
        data object HideSuccessDonation : Action
        data object BackClick : Action
    }

    sealed interface Event : Base.Event {
        data object GoBack : Event
        data class StartPurchaseFlow(val productId: String) : Event
    }
}