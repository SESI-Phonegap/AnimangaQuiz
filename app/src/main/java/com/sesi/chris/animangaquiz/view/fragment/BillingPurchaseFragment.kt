package com.sesi.chris.animangaquiz.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.PurchasesUpdatedListener
import com.sesi.chris.animangaquiz.databinding.FragmentPurchaseBinding
import com.sesi.chris.animangaquiz.view.adapter.BillingProductsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BillingPurchaseFragment : Fragment(), BillingProductsAdapter.RvAction {
    private lateinit var binding: FragmentPurchaseBinding
    private val viewModel: BillingPurchaseViewModel by viewModels()
    private var adapter = BillingProductsAdapter(this)
    private lateinit var billingClient:BillingClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPurchaseBinding.inflate(inflater, container, false)
        observers()
        initBillingClient()
        return binding.root
    }

    private fun initBillingClient() {
        val purchasesUpdatedListener =
            PurchasesUpdatedListener { billingResult, purchases ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                    Toast.makeText(requireContext(),"Compra exitosa", Toast.LENGTH_LONG).show()

                } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                    Toast.makeText(requireContext(),"Compra cancelada", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(),"Compra cancelada", Toast.LENGTH_LONG).show()
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
    }

    override fun onClick(productDetails: ProductDetails) {
        viewModel.purchaseProduct(productDetails, this.billingClient, requireActivity())
    }
}