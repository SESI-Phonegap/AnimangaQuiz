package com.sesi.chris.animangaquiz.view.adapter;



import com.android.billingclient.api.BillingClient;
import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.billing.BillingProvider;

public class SmallGemsDelegate extends UiManagingDelegate{
    public static final String SKU_ID = "sku_small_gems";

    public SmallGemsDelegate(BillingProvider billingProvider) {
        super(billingProvider);
    }

    @Override
    public String getType() {
        return BillingClient.SkuType.INAPP;
    }

    @Override
    public void onBindViewHolder(SkuRowData data, RowViewHolder holder) {
        super.onBindViewHolder(data, holder);
        holder.getButton().setText(R.string.btn_comprar);
        holder.getSkuIcon().setImageResource(R.drawable.gems_costal_small);
    }

    @Override
    public void onButtonClicked(SkuRowData data) {
        super.onButtonClicked(data);
    }
}
