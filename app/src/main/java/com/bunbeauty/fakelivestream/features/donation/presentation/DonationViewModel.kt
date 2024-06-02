package com.bunbeauty.fakelivestream.features.donation.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.fakelivestream.common.analytics.AnalyticsManager
import com.bunbeauty.fakelivestream.common.presentation.BaseViewModel
import com.bunbeauty.fakelivestream.features.donation.domain.GetDonationProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DonationViewModel @Inject constructor(
    private val getDonationProductsUseCase: GetDonationProductsUseCase,
    private val analyticsManager: AnalyticsManager,
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
            is Donation.Action.DonationClick -> {
                analyticsManager.trackOpenDonation(productId = action.productId)
                setState {
                    copy(
                        shownProduct = productList.find { product ->
                            product.id == action.productId
                        }
                    )
                }
            }

            Donation.Action.HideDonation -> {
                setState {
                    copy(shownProduct = null)
                }
            }

            is Donation.Action.DonateClick -> {
                setState {
                    copy(shownProduct = null)
                }
            }

            is Donation.Action.BackClick -> {
                sendEvent(Donation.Event.GoBack)
            }
        }
    }

}