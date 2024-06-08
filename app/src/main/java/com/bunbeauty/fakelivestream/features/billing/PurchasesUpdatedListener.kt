package com.bunbeauty.fakelivestream.features.billing

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.bunbeauty.fakelivestream.common.analytics.AnalyticsManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PurchasesListener @Inject constructor(
    private val analyticsManager: AnalyticsManager
) : PurchasesUpdatedListener {

    private val mutableSuccessPurchaseFlow = MutableSharedFlow<String>()
    val successPurchaseFlow = mutableSuccessPurchaseFlow.asSharedFlow()

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        when (result.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                val productId = purchases?.firstOrNull()?.products?.firstOrNull() ?: return
                mutableSuccessPurchaseFlow.tryEmit(productId)
                analyticsManager.trackPurchaseProduct(productId = productId)
            }

            BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED -> {
                analyticsManager.trackFeatureNotSupported()
            }

            BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> {
                analyticsManager.trackServiceDisconnected()
            }

            BillingClient.BillingResponseCode.USER_CANCELED -> {
                analyticsManager.trackUserCanceled()
            }

            BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE -> {
                analyticsManager.trackServiceUnavailable()
            }

            BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> {
                analyticsManager.trackBillingUnavailable()
            }

            BillingClient.BillingResponseCode.ITEM_UNAVAILABLE -> {
                analyticsManager.trackItemUnavailable()
            }

            BillingClient.BillingResponseCode.DEVELOPER_ERROR -> {
                analyticsManager.trackDeveloperError()
            }

            BillingClient.BillingResponseCode.ERROR -> {
                analyticsManager.trackError()
            }

            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                analyticsManager.trackItemAlreadyOwned()
            }

            BillingClient.BillingResponseCode.ITEM_NOT_OWNED -> {
                analyticsManager.trackItemNotOwned()
            }

            BillingClient.BillingResponseCode.NETWORK_ERROR -> {
                analyticsManager.trackNetworkError()
            }
        }
    }

}