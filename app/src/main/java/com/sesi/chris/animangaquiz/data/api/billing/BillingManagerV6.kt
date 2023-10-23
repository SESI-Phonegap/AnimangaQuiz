package com.sesi.chris.animangaquiz.data.api.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
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

class BillingManagerV6 @Inject constructor() {

    fun openConnection(billingClient:BillingClient, action:BillingAction) {
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

    private suspend fun getInAppProducts(billingClient:BillingClient, action:BillingAction) {
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

    fun getBillingInAppProducts(billingClient:BillingClient, action:BillingAction){
        CoroutineScope(Dispatchers.IO).launch{
            getInAppProducts(billingClient, action)
        }
    }

    fun purchaseProduct(billingClient:BillingClient, productDetails: ProductDetails, activity: Activity, action: BillingAction) {
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                .setProductDetails(productDetails)
                // to get an offer token, call ProductDetails.subscriptionOfferDetails()
                // for a list of offers that are available to the user
                //.setOfferToken(selectedOfferToken)
                .build()
        )
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()
        // Launch the billing flow
        billingClient.launchBillingFlow(activity, billingFlowParams)
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