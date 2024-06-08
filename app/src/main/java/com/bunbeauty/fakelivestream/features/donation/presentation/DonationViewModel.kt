package com.bunbeauty.fakelivestream.features.donation.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.fakelivestream.common.analytics.AnalyticsManager
import com.bunbeauty.fakelivestream.common.presentation.BaseViewModel
import com.bunbeauty.fakelivestream.features.billing.PurchasesListener
import com.bunbeauty.fakelivestream.features.donation.domain.GetDonationProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DonationViewModel @Inject constructor(
    private val getDonationProductsUseCase: GetDonationProductsUseCase,
    private val analyticsManager: AnalyticsManager,
    private val purchasesListener: PurchasesListener,
) : BaseViewModel<Donation.State, Donation.Action, Donation.Event>(
    initState = {
        Donation.State()
    }
) {

    init {
        getProducts()
        observeSuccess()
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
                sendEvent(Donation.Event.StartPurchaseFlow(action.productId))
                setState {
                    copy(shownProduct = null)
                }
            }

            Donation.Action.HideSuccessDonation -> {
                setState {
                    copy(shownSuccessDonation = null)
                }
            }

            is Donation.Action.BackClick -> {
                sendEvent(Donation.Event.GoBack)
            }
        }
    }

    private fun getProducts() {
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

    private fun observeSuccess() {
        purchasesListener.successPurchaseFlow.onEach { productId ->
            setState {
                copy(
                    shownSuccessDonation = productList.find { product ->
                        product.id == productId
                    }
                )
            }
        }.launchIn(viewModelScope)
    }

}