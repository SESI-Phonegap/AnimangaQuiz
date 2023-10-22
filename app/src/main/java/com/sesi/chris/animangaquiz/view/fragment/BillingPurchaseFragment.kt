package com.sesi.chris.animangaquiz.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sesi.chris.animangaquiz.databinding.FragmentPurchaseBinding
import com.sesi.chris.animangaquiz.view.adapter.BillingProductsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BillingPurchaseFragment : Fragment() {
    private lateinit var binding: FragmentPurchaseBinding
    private val viewModel: BillingPurchaseViewModel by viewModels()
    private var adapter = BillingProductsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPurchaseBinding.inflate(inflater, container, false)
        observers()

        viewModel.connect()
        return binding.root
    }

    private fun observers() {
        viewModel.isConnected.observe(viewLifecycleOwner) {
            viewModel.getBillingProducts()
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
}