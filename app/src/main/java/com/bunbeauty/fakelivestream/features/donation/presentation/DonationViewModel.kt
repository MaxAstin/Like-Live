package com.bunbeauty.fakelivestream.features.donation.presentation

import com.bunbeauty.fakelivestream.common.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DonationViewModel @Inject constructor() : BaseViewModel<Donation.State, Donation.Action, Donation.Event>(
    initState = {
        Donation.State()
    }
) {

    override fun onAction(action: Donation.Action) {
        when (action) {
            is Donation.Action.OptionClick -> {

            }

            is Donation.Action.BackClick -> {
                sendEvent(Donation.Event.GoBack)
            }
        }
    }

}