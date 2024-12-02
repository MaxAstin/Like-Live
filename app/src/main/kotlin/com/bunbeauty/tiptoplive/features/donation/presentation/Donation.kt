package com.bunbeauty.tiptoplive.features.donation.presentation

import com.bunbeauty.tiptoplive.common.presentation.Base
import com.bunbeauty.tiptoplive.features.billing.Product
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