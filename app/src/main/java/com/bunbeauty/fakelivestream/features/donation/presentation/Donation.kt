package com.bunbeauty.fakelivestream.features.donation.presentation

import com.bunbeauty.fakelivestream.common.presentation.Base
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

interface Donation {

    data class State(
        val options: ImmutableList<Option> = Option.entries.toImmutableList()
    ) : Base.State

    enum class Option(val count: Int) {
        ONE_DOLLAR(count = 1),
        THREE_DOLLAR(count = 3),
        FIVE_DOLLAR(count = 5),
        TEN_DOLLAR(count = 10),
        TWENTY_DOLLAR(count = 20),
        HUNDRED_DOLLAR(count = 100);

        val text: String
            get() = "\$$count"
    }

    sealed interface Action : Base.Action {
        data class OptionClick(val option: Option): Action
        data object BackClick: Action
    }

    sealed interface Event : Base.Event {
        data object GoBack : Event
    }
}