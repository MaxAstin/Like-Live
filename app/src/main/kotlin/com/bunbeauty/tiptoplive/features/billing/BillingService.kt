package com.bunbeauty.tiptoplive.features.billing

import android.app.Activity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.queryProductDetails
import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class BillingService @Inject constructor(
    private val billingClient: BillingClient,
    private val analyticsManager: AnalyticsManager,
) {

    suspend fun getOneTypeProducts(ids: List<String>): List<Product>? {
        return getProducts(
            type = BillingClient.ProductType.INAPP,
            ids = ids
        )
    }

    suspend fun getSubscriptionProducts(ids: List<String>): List<Product>? {
        return getProducts(
            type = BillingClient.ProductType.SUBS,
            ids = ids
        )
    }

    suspend fun launchOneTypeProductFlow(
        activity: Activity,
        id: String
    ): Boolean {
        val product = getProductDetails(
            type = BillingClient.ProductType.INAPP,
            ids = listOf(id)
        )?.firstOrNull()
        if (product == null) {
            analyticsManager.trackProductNotFound(productId = id)
            return false
        }

        val productDetailsParams = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(product)
                .build()
        )
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParams)
            .build()
        val result = billingClient.launchBillingFlow(activity, billingFlowParams)
        val isSuccessful = result.responseCode == BillingClient.BillingResponseCode.OK
        if (isSuccessful) {
            analyticsManager.trackStartBillingFlow(productId = id)
        } else {
            analyticsManager.trackFailBillingFlow(productId = id)
        }

        return isSuccessful
    }

    private suspend fun getProducts(
        type: String,
        ids: List<String>
    ): List<Product>? {
        val isSuccessful = init()
        return if (isSuccessful) {
            getProductDetails(
                type = type,
                ids = ids,
            )?.mapNotNull { productDetails ->
                productDetails.oneTimePurchaseOfferDetails?.formattedPrice?.let { price ->
                    Product(
                        id = productDetails.productId,
                        name = productDetails.name,
                        description = productDetails.description,
                        price = price,
                    )
                }
            }
        } else {
            emptyList()
        }
    }

    private suspend fun init(): Boolean {
        if (billingClient.isReady) {
            return true
        }

        return suspendCoroutine { continuation ->
            billingClient.startConnection(
                object : BillingClientStateListener {
                    override fun onBillingSetupFinished(billingResult: BillingResult) {
                        val isSuccessful = billingResult.responseCode == BillingClient.BillingResponseCode.OK
                        try {
                            continuation.resume(isSuccessful)
                        } catch (exception: Exception) {
                            // TODO handle
                        }
                    }

                    override fun onBillingServiceDisconnected() {
                        try {
                            continuation.resume(false)
                        } catch (exception: Exception) {
                            // TODO handle
                        }
                    }
                }
            )
        }
    }

    private suspend fun getProductDetails(type: String, ids: List<String>): List<ProductDetails>? {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                ids.map { id ->
                    getProductDetailsParams(
                        id = id,
                        type = type,
                    )
                }
            )
            .build()

        return withContext(Dispatchers.IO) {
            val result = billingClient.queryProductDetails(params)
            if (result.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                result.productDetailsList
            } else {
                null
            }
        }
    }

    private fun getProductDetailsParams(
        type: String,
        id: String,
    ): QueryProductDetailsParams.Product {
        return QueryProductDetailsParams.Product.newBuilder()
            .setProductId(id)
            .setProductType(type)
            .build()
    }

}