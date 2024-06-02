package com.bunbeauty.fakelivestream.features.donation.presentation

import com.bunbeauty.fakelivestream.common.presentation.Base
import com.bunbeauty.fakelivestream.features.billing.Product
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

interface Donation {

    data class State(
        val productList: ImmutableList<Product> = persistentListOf()
    ) : Base.State

    sealed interface Action : Base.Action {
        data class OptionClick(val productId: String): Action
        data object BackClick: Action
    }

    sealed interface Event : Base.Event {
        data object GoBack : Event
    }
}