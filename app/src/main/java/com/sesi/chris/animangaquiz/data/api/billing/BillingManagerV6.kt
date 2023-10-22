package com.sesi.chris.animangaquiz.data.api.billing

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetailsResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.queryProductDetails
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BillingManagerV6 {

    private var billingClient: BillingClient
    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            // To be implemented in a later section.
        }

    @Inject
    constructor(@ApplicationContext context: Context) {
        billingClient = BillingClient.newBuilder(context).setListener(purchasesUpdatedListener)
            .enablePendingPurchases().build()
    }

    fun openConnection(action:BillingAction) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    action.isConnected(true)
                }
            }

            override fun onBillingServiceDisconnected() {
                action.isConnected(false)
            }
        })
    }

    private suspend fun getInAppProducts(action:BillingAction) {
        val productList = ArrayList<QueryProductDetailsParams.Product>()
        productList.add(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(SMALL_GEMS_5000)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )
        productList.add(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(MED_GEMS_10000)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )
        productList.add(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(L_GEMS_20000)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )
        val params = QueryProductDetailsParams.newBuilder()
        params.setProductList(productList)

        // leverage queryProductDetails Kotlin extension function
        val products = withContext(Dispatchers.IO) {
            billingClient.queryProductDetails(params.build())
        }
        action.getProducts(products)
    }

    fun getBillingInAppProducts(action:BillingAction){
        CoroutineScope(Dispatchers.IO).launch{
            getInAppProducts(action)
        }
    }

    companion object {
        const val SMALL_GEMS_5000 = "sku_small_gems"
        const val MED_GEMS_10000 = "sku_med_gems"
        const val L_GEMS_20000 = "sku_large_gems"
    }
}

interface BillingAction{
    fun isConnected(connected:Boolean)
    fun getProducts(products:ProductDetailsResult)
}