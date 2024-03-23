package com.sesi.chris.animangaquiz.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.PurchasesUpdatedListener
import com.sesi.chris.animangaquiz.data.api.billing.BillingManagerV6.Companion.L_GEMS_20000
import com.sesi.chris.animangaquiz.data.api.billing.BillingManagerV6.Companion.MED_GEMS_10000
import com.sesi.chris.animangaquiz.data.api.client.QuizClient
import com.sesi.chris.animangaquiz.data.dto.UpdateGemsDto
import com.sesi.chris.animangaquiz.data.dto.UserDto
import com.sesi.chris.animangaquiz.databinding.FragmentPurchaseBinding
import com.sesi.chris.animangaquiz.interactor.LoginInteractor
import com.sesi.chris.animangaquiz.presenter.LoginPresenter
import com.sesi.chris.animangaquiz.presenter.LoginPresenter.ViewLogin
import com.sesi.chris.animangaquiz.view.adapter.BillingProductsAdapter
import com.sesi.chris.animangaquiz.view.utils.Preference
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BillingPurchaseFragment : Fragment(), BillingProductsAdapter.RvAction {
    private lateinit var binding: FragmentPurchaseBinding
    private val viewModel: BillingPurchaseViewModel by viewModels()
    private var adapter = BillingProductsAdapter(this)
    private lateinit var billingClient:BillingClient
    private lateinit var userData: UserDto

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPurchaseBinding.inflate(inflater, container, false)
        userData = Preference.getObject(Preference.USER_DATA, UserDto::class.java, requireContext())
        observers()
        initBillingClient()
        return binding.root
    }

    private fun initBillingClient() {
        val purchasesUpdatedListener =
            PurchasesUpdatedListener { billingResult, purchases ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                    Toast.makeText(requireContext(), "Compra exitosa", Toast.LENGTH_LONG).show()
                    val gemsData = UpdateGemsDto(userData.email, userData.pass, userData.userId)
                    val purchase = purchases.first()
                    if (purchase.products.contains(L_GEMS_20000)) {
                        gemsData.gems = 20000
                    } else if (purchase.products.contains(MED_GEMS_10000)) {
                        gemsData.gems = 10000
                    } else {
                        gemsData.gems = 5000
                    }
                    viewModel.updateGems(gemsData)
                } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                    Toast.makeText(requireContext(), "Compra cancelada", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Compra cancelada", Toast.LENGTH_LONG).show()
                }
            }
        this.billingClient =
            BillingClient.newBuilder(requireContext()).setListener(purchasesUpdatedListener)
                .enablePendingPurchases().build()
        viewModel.connect(billingClient)
    }

    private fun observers() {
        viewModel.isConnected.observe(viewLifecycleOwner) {
            viewModel.getBillingProducts(this.billingClient)
        }
        viewModel.products.observe(viewLifecycleOwner) {
            if (!it.productDetailsList.isNullOrEmpty()) {
                adapter.setProducts(it.productDetailsList!!)
                binding.list.adapter = adapter
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBarPurchase.visibility = if (it) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        viewModel.msg.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        viewModel.isUpdated.observe(viewLifecycleOwner) {
            isUpdated ->
            if (isUpdated){
                Log.i("BillingFragment", "Se agregaron las gemas")
            } else {
                Log.i("BillingFragment", "Error")
            }
        }
    }

    override fun onClick(productDetails: ProductDetails) {
        viewModel.purchaseProduct(productDetails, this.billingClient, requireActivity())
    }
}