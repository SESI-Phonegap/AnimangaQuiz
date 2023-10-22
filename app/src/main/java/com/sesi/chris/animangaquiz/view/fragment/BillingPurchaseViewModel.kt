package com.sesi.chris.animangaquiz.view.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.billingclient.api.ProductDetailsResult
import com.sesi.chris.animangaquiz.data.api.billing.BillingAction
import com.sesi.chris.animangaquiz.data.api.billing.BillingManagerV6
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BillingPurchaseViewModel @Inject constructor(
    private val billingManager: BillingManagerV6
) : ViewModel(), BillingAction {

    var isConnected = MutableLiveData<Boolean>()
    var isLoading = MutableLiveData<Boolean>()
    var products = MutableLiveData<ProductDetailsResult>()
    fun connect() {
        isLoading.postValue(true)
        billingManager.openConnection(this)
    }

    fun getBillingProducts() {
        billingManager.getBillingInAppProducts(this)
    }

    override fun isConnected(connected: Boolean) {
        this.isLoading.postValue(false)
        this.isConnected.postValue(connected)
    }

    override fun getProducts(products: ProductDetailsResult) {
        this.products.postValue(products)
        this.isLoading.postValue(false)
    }

}