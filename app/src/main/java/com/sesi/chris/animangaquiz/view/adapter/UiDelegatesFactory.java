/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sesi.chris.animangaquiz.view.adapter;

import com.android.billingclient.api.BillingClient.SkuType;
import com.sesi.chris.animangaquiz.data.api.billing.BillingProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This factory is responsible to finding the appropriate delegate for Ui rendering and calling
 * corresponding method on it.
 */
public class UiDelegatesFactory {
    private final Map<String, UiManagingDelegate> uiDelegates;

    public UiDelegatesFactory(BillingProvider provider) {
        uiDelegates = new HashMap<>();
        uiDelegates.put(SmallGemsDelegate.SKU_ID, new SmallGemsDelegate(provider));
        uiDelegates.put(MedGemsDelegate.SKU_ID, new MedGemsDelegate(provider));
        uiDelegates.put(LargeGemsDelegate.SKU_ID, new LargeGemsDelegate(provider));

        //     uiDelegates.put(GoldMonthlyDelegate.SKU_ID, new GoldMonthlyDelegate(provider));
   //     uiDelegates.put(GoldYearlyDelegate.SKU_ID, new GoldYearlyDelegate(provider));

    }

    /**
     * Returns the list of all SKUs for the billing type specified
     */
    public final List<String> getSkuList(@SkuType String billingType) {
        List<String> result = new ArrayList<>();
        for (String skuId : uiDelegates.keySet()) {
            UiManagingDelegate delegate = uiDelegates.get(skuId);
            if (delegate.getType().equals(billingType)) {
                result.add(skuId);
            }
        }
        return result;
    }

    public void onBindViewHolder(SkuRowData data, RowViewHolder holder) {
        uiDelegates.get(data.getSku()).onBindViewHolder(data, holder);
    }

    public void onButtonClicked(SkuRowData data) {
        uiDelegates.get(data.getSku()).onButtonClicked(data);
    }
}
