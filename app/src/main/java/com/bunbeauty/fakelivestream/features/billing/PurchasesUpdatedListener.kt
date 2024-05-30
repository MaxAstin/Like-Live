package com.bunbeauty.fakelivestream.features.billing

import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import javax.inject.Inject

class PurchasesListener @Inject constructor(

) : PurchasesUpdatedListener {

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {

    }

}