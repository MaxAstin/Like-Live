package com.bunbeauty.fakelivestream.features.donation.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.fakelivestream.common.presentation.BaseViewModel
import com.bunbeauty.fakelivestream.features.donation.domain.GetDonationProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DonationViewModel @Inject constructor(
    private val getDonationProductsUseCase: GetDonationProductsUseCase
) : BaseViewModel<Donation.State, Donation.Action, Donation.Event>(
    initState = {
        Donation.State()
    }
) {

    init {
        viewModelScope.launch {
            getDonationProductsUseCase().let { productList ->
                setState {
                    copy(
                        productList = productList.toImmutableList()
                    )
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