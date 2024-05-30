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

    suspend fun init(): Boolean {
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

    suspend fun getOneTypeProducts(ids: List<String>): List<String>? {
        return getProducts(
            ids = ids,
            type = BillingClient.ProductType.INAPP
        )
    }

    suspend fun getSubscriptionProducts(ids: List<String>): List<String>? {
        return getProducts(
            ids = ids,
            type = BillingClient.ProductType.SUBS
        )
    }

    suspend fun launchOneTypeProductFlow(
        id: String,
        activity: Activity
    ): Boolean {
        val product = getProductDetails(
            ids = listOf(id),
            type = BillingClient.ProductType.INAPP
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
        ids: List<String>,
        type: String,
    ): List<String>? {
        return getProductDetails(
            ids = ids,
            type = type
        )?.map { productDetails ->
            productDetails.name
        }
    }

    private suspend fun getProductDetails(
        ids: List<String>,
        type: String,
    ): List<ProductDetails>? {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                ids.map { id ->
                    getOneTypeProduct(
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

    private fun getOneTypeProduct(
        id: String,
        type: String
    ): QueryProductDetailsParams.Product {
        return QueryProductDetailsParams.Product.newBuilder()
            .setProductId(id)
            .setProductType(type)
            .build()
    }

}