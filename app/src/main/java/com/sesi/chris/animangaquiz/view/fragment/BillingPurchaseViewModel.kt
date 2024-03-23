package com.sesi.chris.animangaquiz.view.fragment

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.ProductDetailsResult
import com.sesi.chris.animangaquiz.data.api.billing.BillingAction
import com.sesi.chris.animangaquiz.data.api.billing.BillingManagerV6
import com.sesi.chris.animangaquiz.data.dto.UpdateGemsDto
import com.sesi.chris.animangaquiz.data.repository.UserAction
import com.sesi.chris.animangaquiz.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BillingPurchaseViewModel @Inject constructor(
    private val billingManager: BillingManagerV6,
    private val userRepository: UserRepository
) : ViewModel(), BillingAction, UserAction {

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> = _isConnected
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _products = MutableLiveData<ProductDetailsResult>()
    val products: LiveData<ProductDetailsResult> = _products
    private val _msg = MutableLiveData<String>()
    val msg: LiveData<String> = _msg
    private val _isUpdated = MutableLiveData<Boolean>()
    val isUpdated: LiveData<Boolean> = _isUpdated
    fun connect(billingClient: BillingClient) {
        _isLoading.postValue(true)
        billingManager.openConnection(billingClient, this)
    }

    fun updateGems(gemsInfo:UpdateGemsDto) {
        this._isLoading.postValue(true)
        userRepository.updateGem(gemsInfo, this)
    }

    fun getBillingProducts(billingClient: BillingClient) {
        billingManager.getBillingInAppProducts(billingClient, this)
    }

    fun purchaseProduct(
        productDetails: ProductDetails,
        billingClient: BillingClient,
        activity: Activity
    ) {
        billingManager.purchaseProduct(billingClient, productDetails, activity, this)
    }

    override fun isConnected(connected: Boolean) {
        this._isLoading.postValue(false)
        this._isConnected.postValue(connected)
    }

    override fun getProducts(products: ProductDetailsResult) {
        this._products.postValue(products)
        this._isLoading.postValue(false)
    }

    override fun showError(error: String) {
        this._isLoading.postValue(false)
        this._msg.postValue(error)
    }

    override fun isUpdated(isUpdated: Boolean) {
        this._isLoading.postValue(false)
        this._isUpdated.postValue(isUpdated)
    }

}