package com.bunbeauty.fakelivestream.features.billing

import android.app.Activity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.queryProductDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BillingService @Inject constructor(
    private val billingClient: BillingClient
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
        )?.firstOrNull() ?: return false

        val productDetailsParams = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(product)
                .build()
        )
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParams)
            .build()

        billingClient.launchBillingFlow(activity, billingFlowParams)
        return true
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
                        continuation.resume(isSuccessful)
                    }

                    override fun onBillingServiceDisconnected() {
                        continuation.resume(false)
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