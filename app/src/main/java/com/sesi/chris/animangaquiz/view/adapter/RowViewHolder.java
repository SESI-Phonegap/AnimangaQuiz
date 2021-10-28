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

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sesi.chris.animangaquiz.R;


/**
 * ViewHolder for quick access to row's views
 */
public final class RowViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView description;
    private TextView price;
    private Button button;
    private ImageView skuIcon;

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public TextView getDescription() {
        return description;
    }

    public void setDescription(TextView description) {
        this.description = description;
    }

    public TextView getPrice() {
        return price;
    }

    public void setPrice(TextView price) {
        this.price = price;
    }

    public Button getButton() {
        return button;
    }

    public ImageView getSkuIcon() {
        return skuIcon;
    }

    /**
     * Handler for a button click on particular row
     */
    public interface OnButtonClickListener {
        void onButtonClicked(int position);
    }

    public RowViewHolder(final View itemView, final OnButtonClickListener clickListener) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        price = itemView.findViewById(R.id.price);
        description = itemView.findViewById(R.id.description);
        skuIcon = itemView.findViewById(R.id.sku_icon);
        button = itemView.findViewById(R.id.state_button);
        if (button != null) {
            button.setOnClickListener(view -> clickListener.onButtonClicked(getAdapterPosition()));
        }
    }
}
