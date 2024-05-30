package com.bunbeauty.fakelivestream.features.donation.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.fakelivestream.common.presentation.BaseViewModel
import com.bunbeauty.fakelivestream.features.billing.BillingService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DonationViewModel @Inject constructor(
    private val billingService: BillingService
) : BaseViewModel<Donation.State, Donation.Action, Donation.Event>(
    initState = {
        Donation.State()
    }
) {

    init {
        viewModelScope.launch {
            val isSuccessful = billingService.init()
            if (isSuccessful) {
                val products = billingService.getOneTypeProducts(
                    ids = Donation.Option.entries.map { option ->
                        option.name
                    }
                )
                if (products != null) {
                    setState {
                        copy(
                            options = products.mapNotNull { product ->
                                Donation.Option.entries.find {option ->
                                    option.name == product
                                }
                            }.toImmutableList()
                        )
                    }
                }
            }
        }
    }

    override fun onAction(action: Donation.Action) {
        when (action) {
            is Donation.Action.OptionClick -> {
                // TODO call analytics
            }

            is Donation.Action.BackClick -> {
                sendEvent(Donation.Event.GoBack)
            }
        }
    }

}