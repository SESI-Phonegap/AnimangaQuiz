package com.sesi.chris.animangaquiz.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.ProductDetails
import com.sesi.chris.animangaquiz.R
import com.sesi.chris.animangaquiz.data.api.billing.BillingManagerV6.Companion.L_GEMS_20000
import com.sesi.chris.animangaquiz.data.api.billing.BillingManagerV6.Companion.MED_GEMS_10000
import com.sesi.chris.animangaquiz.data.api.billing.BillingManagerV6.Companion.SMALL_GEMS_5000
import com.sesi.chris.animangaquiz.databinding.SkuDetailsRowBinding

class BillingProductsAdapter() : RecyclerView.Adapter<BillingProductsAdapter.BillingProductsViewHolder>() {

    private var billingProducts =  listOf<ProductDetails>()
    class BillingProductsViewHolder(val binding: SkuDetailsRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingProductsViewHolder {
        return BillingProductsViewHolder(SkuDetailsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BillingProductsViewHolder, position: Int) {
        with(holder) {
            with(billingProducts[position]) {
                binding.title.text = name
                binding.description.text = description
                binding.price.text = oneTimePurchaseOfferDetails!!.formattedPrice
                when (productId) {
                    SMALL_GEMS_5000 -> binding.skuIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.gems_costal_small
                        )
                    )

                    MED_GEMS_10000 -> binding.skuIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.gems_costal_med
                        )
                    )

                    L_GEMS_20000 -> binding.skuIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.gems_costal
                        )
                    )
                }
            }
        }
    }

    fun setProducts(products: List<ProductDetails>){
        this.billingProducts = products
    }
    override fun getItemCount(): Int {
        return billingProducts.size
    }

}