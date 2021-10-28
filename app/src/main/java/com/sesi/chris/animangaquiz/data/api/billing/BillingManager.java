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
package com.sesi.chris.animangaquiz.data.api.billing;


import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClient.FeatureType;
import com.android.billingclient.api.BillingClient.SkuType;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.Purchase.PurchasesResult;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Handles all the interactions with Play Store (via Billing library), maintains connection to
 * it through BillingClient and caches temporary states/data if needed
 */
public class BillingManager implements PurchasesUpdatedListener {
    // Default value of mBillingClientResponseCode until BillingManager was not yeat initialized
    public static final int BILLING_MANAGER_NOT_INITIALIZED  = -1;

    private static final String TAG = "BillingManager";

    private static final String GEMS_5K = "sku_small_gems";
    private static final String GEMS_10K = "sku_med_gems";
    private static final String GEMS_20K = "sku_large_gems";

    private static final String[] INAPP_SKUS = new String[] {GEMS_5K, GEMS_10K, GEMS_20K};
    private static final long SKU_DETAILS_REQUERY_TIME = 1000L * 60L * 60L * 4L; // 4 hours
    // when was the last successful SkuDetailsResponse?
    private long skuDetailsResponseTime = -SKU_DETAILS_REQUERY_TIME;
    final private Map<String, MutableLiveData<SkuState>> skuStateMap = new HashMap<>();
    // Observables that are used to communicate state.
    final private Set<Purchase> purchaseConsumptionInProcess = new HashSet<>();
    final private SingleMediatorLiveEvent<List<String>> newPurchase = new SingleMediatorLiveEvent<>();
    final private SingleMediatorLiveEvent<List<String>> purchaseConsumed =
            new SingleMediatorLiveEvent<>();
    final private Map<String, MutableLiveData<SkuDetails>> skuDetailsLiveDataMap = new HashMap<>();
    private List<String> listSkus;

    // SKUs to auto-consume
    final private Set<String> knownAutoConsumeSKUs;

    /** A reference to BillingClient **/
    private BillingClient mBillingClient;

    /**
     * True if billing service is connected now.
     */
    private boolean mIsServiceConnected;

    private final BillingUpdatesListener mBillingUpdatesListener;

    private final Activity mActivity;

    private final List<Purchase> mPurchases = new ArrayList<>();

    private Set<String> mTokensToBeConsumed;

    private int mBillingClientResponseCode = BILLING_MANAGER_NOT_INITIALIZED;

    /* BASE_64_ENCODED_PUBLIC_KEY should be YOUR APPLICATION'S PUBLIC KEY
     * (that you got from the Google Play developer console). This is not your
     * developer public key, it's the *app-specific* public key.
     *
     * Instead of just storing the entire literal string here embedded in the
     * program,  construct the key at runtime from pieces or
     * use bit manipulation (for example, XOR with some other string) to hide
     * the actual key.  The key itself is not secret information, but we don't
     * want to make it easy for an attacker to replace the public key with one
     * of their own and then fake messages from the server.
     */
    private static final String BASE_64_ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkSDwttXbl4nyb/4ddc3jabhdoaxZ8kG3CAvP7d/yElmgWx3I7/8Gu1XrhLD4utFXVJF4tWKm1uRGzCjOSnAF3IavTY4mXqCKilR2ZA60HihQbPI1buyGhFhI/c0dqxyoft3THu1T9k8qyIG1Z+yZe3Me63nln/4HYWEZMCTXlsgLObnROXv5NplXbqZ/2l4wla6m5Nu0LxJmJAeT3ppwJmsuRXdxNRwDwPP8D+7rHUNsysSfcHPzvSp4IRB35uGvKv3l332mkw+EHwug6r1nyd+kM/KDMy/Z5gWirX+tWW90gfOsJjUHgQiYVUbbN1AXeIOeJoRiEYtS0RYamp3zcwIDAQAB";

    /**
     * Listener to the updates that happen when purchases list was updated or consumption of the
     * item was finished
     */
    public interface BillingUpdatesListener {
        void onBillingClientSetupFinished();
        void onConsumeFinished(String token, int result);
        void onPurchasesUpdated(List<Purchase> purchases);
    }

    /**
     * Listener for the Billing client state to become connected
     */
    public interface ServiceConnectedListener {
        void onServiceConnected(int resultCode);
    }

    public BillingManager(Activity activity, final BillingUpdatesListener updatesListener) {
        Log.d(TAG, "Creating Billing client.");
        mActivity = activity;
        mBillingUpdatesListener = updatesListener;
        listSkus = Arrays.asList(INAPP_SKUS);

        mBillingClient = BillingClient.newBuilder(mActivity)
                .setListener(this)
                .enablePendingPurchases()
                .build();
        knownAutoConsumeSKUs = new HashSet<>();
        Log.d(TAG, "Starting setup.");

        // Start setup. This is asynchronous and the specified listener will be called
        // once setup completes.
        // It also starts to report all the new purchases through onPurchasesUpdated() callback.
        startServiceConnection(() -> {
            // Notifying the listener that billing client is ready
            mBillingUpdatesListener.onBillingClientSetupFinished();
            // IAB is fully set up. Now, let's get an inventory of stuff we own.
            Log.d(TAG, "Setup successful. Querying inventory.");
            queryPurchases();
        });
    }

    /**
     * Handle a callback that purchases were updated from the Billing library
     */
    @Override
    public void onPurchasesUpdated(BillingResult resultCode, List<Purchase> purchases) {
        if (resultCode.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
            mBillingUpdatesListener.onPurchasesUpdated(mPurchases);
        } else if (resultCode.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.i(TAG, "onPurchasesUpdated() - user cancelled the purchase flow - skipping");
        } else {
            Log.w(TAG, "onPurchasesUpdated() got unknown resultCode: " + resultCode);
        }
    }

    /**
     * Start a purchase flow
     */
    public void initiatePurchaseFlow(final String skuId, final @SkuType String billingType, SkuDetails details) {
        initiatePurchaseFlow(skuId, null, billingType, details);
    }

    /**
     * Start a purchase or subscription replace flow
     */
    public void initiatePurchaseFlow(final String skuId, final ArrayList<String> oldSkus,
            final @SkuType String billingType, SkuDetails skuDetails) {
        Runnable purchaseFlowRequest = () -> {
            Log.d(TAG, "Launching in-app purchase flow. Replace old SKU? " + (oldSkus != null));
            BillingFlowParams purchaseParams = BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build();
            mBillingClient.launchBillingFlow(mActivity, purchaseParams);
        };

        executeServiceRequest(purchaseFlowRequest);
    }

    public Context getContext() {
        return mActivity;
    }

    /**
     * Clear the resources
     */
    public void destroy() {
        Log.d(TAG, "Destroying the manager.");

        if (mBillingClient != null && mBillingClient.isReady()) {
            mBillingClient.endConnection();
            mBillingClient = null;
        }
    }

    public void querySkuDetailsAsync(@SkuType final String itemType, final List<String> skuList,
                                     final SkuDetailsResponseListener listener) {
        // Creating a runnable from the request to use it inside our connection retry policy below
        Runnable queryRequest = () -> {
            // Query the purchase async
            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(skuList).setType(itemType);
            mBillingClient.querySkuDetailsAsync(params.build(),
                    (responseCode, skuDetailsList) -> listener.onSkuDetailsResponse(responseCode, skuDetailsList));
        };

        executeServiceRequest(queryRequest);
    }

    public void consumeAsync(final String purchaseToken) {
        // If we've already scheduled to consume this token - no action is needed (this could happen
        // if you received the token when querying purchases inside onReceive() and later from
        // onActivityResult()
        if (mTokensToBeConsumed == null) {
            mTokensToBeConsumed = new HashSet<>();
        } else if (mTokensToBeConsumed.contains(purchaseToken)) {
            Log.i(TAG, "Token was already scheduled to be consumed - skipping...");
            return;
        }
        mTokensToBeConsumed.add(purchaseToken);

        // Generating Consume Response listener
        final ConsumeResponseListener onConsumeListener = (responseCode, purchaseToken1) ->
            // If billing service was disconnected, we try to reconnect 1 time
            // (feel free to introduce your retry policy here).
            mBillingUpdatesListener.onConsumeFinished(purchaseToken1, responseCode.getResponseCode());

        ConsumeParams consumeParams = ConsumeParams
                .newBuilder()
                .setPurchaseToken(purchaseToken)
                .build();
        // Creating a runnable from the request to use it inside our connection retry policy below
        Runnable consumeRequest = () ->
            // Consume the purchase async
            mBillingClient.consumeAsync(consumeParams, onConsumeListener);


        executeServiceRequest(consumeRequest);
    }

    /**
     * Returns the value Billing client response code or BILLING_MANAGER_NOT_INITIALIZED if the
     * clien connection response was not received yet.
     */
    public int getBillingClientResponseCode() {
        return mBillingClientResponseCode;
    }

    /**
     * Handles the purchase
     * <p>Note: Notice that for each purchase, we check if signature is valid on the client.
     * It's recommended to move this check into your backend.
     * See
     * </p>
     * @param purchase Purchase to be handled
     */
    private void handlePurchase(Purchase purchase) {
        Log.d(TAG, "Got a verified purchase: " + purchase);
        mPurchases.add(purchase);
    }

    /**
     * Handle a result from querying of purchases and report an updated list to the listener
     */
    private void onQueryPurchasesFinished(PurchasesResult result) {
        // Have we been disposed of in the meantime? If so, or bad result code, then quit
        if (mBillingClient == null || result.getResponseCode() != BillingClient.BillingResponseCode.OK) {
            Log.w(TAG, "Billing client was null or result code (" + result.getResponseCode()
                    + ") was bad - quitting");
            return;
        }

        Log.d(TAG, "Query inventory was successful.");

        // Update the UI and purchases inventory with new list of purchases
        mPurchases.clear();
        onPurchasesUpdated(result.getBillingResult(), result.getPurchasesList());
    }

    /**
     * Checks if subscriptions are supported for current client
     * <p>Note: This method does not automatically retry for RESULT_SERVICE_DISCONNECTED.
     * It is only used in unit tests and after queryPurchases execution, which already has
     * a retry-mechanism implemented.
     * </p>
     */
    public boolean areSubscriptionsSupported() {
        BillingResult result = mBillingClient.isFeatureSupported(FeatureType.SUBSCRIPTIONS);
        if (result.getResponseCode() != BillingClient.BillingResponseCode.OK) {
            Log.w(TAG, "areSubscriptionsSupported() got an error response: " + result.getResponseCode());
        }
        return result.getResponseCode() == BillingClient.BillingResponseCode.OK;
    }

    /**
     * Query purchases across various use cases and deliver the result in a formalized way through
     * a listener
     */
    public void queryPurchases() {

        List<Purchase> listPurchaseInApp = new ArrayList<>();
        //List<Purchase> listPurchaseSubs = new ArrayList<>();
        mBillingClient.queryPurchasesAsync(SkuType.INAPP, (billingResult, list) -> {
            if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK){
                Log.e(TAG, "Problem getting purchases: " +
                        billingResult.getDebugMessage());
            } else {
                processPurchaseList(list,  Arrays.asList(INAPP_SKUS));
            }
        });

        if (areSubscriptionsSupported()) {
            mBillingClient.queryPurchasesAsync(SkuType.SUBS, new PurchasesResponseListener() {
                @Override
                public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
                    long time = System.currentTimeMillis();
                    Log.i(TAG, "Querying purchases and subscriptions elapsed time: "
                            + (System.currentTimeMillis() - time) + "ms");
                    Log.i(TAG, "Querying subscriptions result code: "
                            + billingResult.getResponseCode()
                            + " res: " + list.size());
                    listPurchaseInApp.addAll(list);
                }
            });
        }

        Runnable queryToExecute = () -> {
            long time = System.currentTimeMillis();

            PurchasesResult purchasesResult = mBillingClient.queryPurchases(SkuType.INAPP);
            Log.i(TAG, "Querying purchases elapsed time: " + (System.currentTimeMillis() - time)
                    + "ms");
            // If there are subscriptions supported, we add subscription rows as well
            if (areSubscriptionsSupported()) {
                PurchasesResult subscriptionResult
                        = mBillingClient.queryPurchases(SkuType.SUBS);
                Log.i(TAG, "Querying purchases and subscriptions elapsed time: "
                        + (System.currentTimeMillis() - time) + "ms");
                Log.i(TAG, "Querying subscriptions result code: "
                        + subscriptionResult.getResponseCode()
                        + " res: " + subscriptionResult.getPurchasesList().size());

                if (subscriptionResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    purchasesResult.getPurchasesList().addAll(
                            subscriptionResult.getPurchasesList());
                } else {
                    Log.e(TAG, "Got an error response trying to query subscription purchases");
                }
            } else if (purchasesResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                Log.i(TAG, "Skipped subscription purchases query since they are not supported");
            } else {
                Log.w(TAG, "queryPurchases() got an error response code: "
                        + purchasesResult.getResponseCode());
            }
            onQueryPurchasesFinished(purchasesResult);
        };

        executeServiceRequest(queryToExecute);
    }

    private void processPurchaseList(List<Purchase> purchases, List<String> skusToUpdate) {
        HashSet<String> updatedSkus = new HashSet<>();
        if (null != purchases) {
            for (final Purchase purchase : purchases) {
                for (String sku : purchase.getSkus()) {
                    final MutableLiveData<SkuState> skuStateLiveData = skuStateMap.get(sku);
                    if (null == skuStateLiveData) {
                        Log.e(TAG, "Unknown SKU " + sku + ". Check to make " +
                                "sure SKU matches SKUS in the Play developer console.");
                        continue;
                    }
                    updatedSkus.add(sku);
                }
                // Global check to make sure all purchases are signed correctly.
                // This check is best performed on your server.
                int purchaseState = purchase.getPurchaseState();
                if (purchaseState == Purchase.PurchaseState.PURCHASED) {
                    if (!isSignatureValid(purchase)) {
                        Log.e(TAG, "Invalid signature on purchase. Check to make " +
                                "sure your public key is correct.");
                        continue;
                    }
                    // only set the purchased state after we've validated the signature.
                    setSkuStateFromPurchase(purchase);
                    boolean isConsumable = false;
                    for (String sku : purchase.getSkus()) {
                        if (knownAutoConsumeSKUs.contains(sku)) {
                            isConsumable = true;
                        } else {
                            if (isConsumable) {
                                Log.e(TAG, "Purchase cannot contain a mixture of consumable" +
                                        "and non-consumable items: " + purchase.getSkus().toString());
                                isConsumable = false;
                                break;
                            }
                        }
                    }
                    if ( isConsumable ) {
                        consumePurchase(purchase);
                    } else if (!purchase.isAcknowledged()) {
                        mBillingClient.acknowledgePurchase(AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build(), billingResult -> {
                            if (billingResult.getResponseCode()
                                    == BillingClient.BillingResponseCode.OK) {
                                // purchase acknowledged
                                for ( String sku : purchase.getSkus() ) {
                                    setSkuState(sku, SkuState.SKU_STATE_PURCHASED_AND_ACKNOWLEDGED);
                                }
                                newPurchase.postValue(purchase.getSkus());
                            }
                        });
                    }
                } else {
                    // make sure the state is set
                    setSkuStateFromPurchase(purchase);
                }
            }
        } else{
            Log.d(TAG, "Empty purchase list.");
        }
        // Clear purchase state of anything that didn't come with this purchase list if this is
        // part of a refresh.
        if (null != skusToUpdate) {
            for (String sku : skusToUpdate) {
                if (!updatedSkus.contains(sku)) {
                    setSkuState(sku, SkuState.SKU_STATE_UNPURCHASED);
                }
            }
        }
    }

    /**
     * Ideally your implementation will comprise a secure server, rendering this check unnecessary.
     * @see [Security]
     */
    private boolean isSignatureValid(@NonNull Purchase purchase) {
        return Security.verifyPurchase(purchase.getOriginalJson(), purchase.getSignature());
    }

    /**
     * Since we (mostly) are getting sku states when we actually make a purchase or update
     * purchases, we keep some internal state when we do things like acknowledge or consume.
     *
     * @param sku         sku to change the state
     * @param newSkuState the new state of the sku.
     */
    private void setSkuState(@NonNull String sku, SkuState newSkuState) {
        MutableLiveData<SkuState> skuStateLiveData = skuStateMap.get(sku);
        if (null == skuStateLiveData) {
            Log.e(TAG, "Unknown SKU " + sku + ". Check to make " +
                    "sure SKU matches SKUS in the Play developer console.");
        } else {
            skuStateLiveData.postValue(newSkuState);
        }
    }

    /**
     * Calling this means that we have the most up-to-date information for a Sku in a purchase
     * object. This uses the purchase state (Pending, Unspecified, Purchased) along with the
     * acknowledged state.
     *
     * @param purchase an up-to-date object to set the state for the Sku
     */
    private void setSkuStateFromPurchase(@NonNull Purchase purchase) {
        for (String purchaseSku:purchase.getSkus()) {
            MutableLiveData<SkuState> skuStateLiveData = skuStateMap.get(purchaseSku);
            if (null == skuStateLiveData) {
                Log.e(TAG, "Unknown SKU " + purchaseSku + ". Check to make " +
                        "sure SKU matches SKUS in the Play developer console.");
            } else {
                switch (purchase.getPurchaseState()) {
                    case Purchase.PurchaseState.PENDING:
                        skuStateLiveData.postValue(SkuState.SKU_STATE_PENDING);
                        break;
                    case Purchase.PurchaseState.UNSPECIFIED_STATE:
                        skuStateLiveData.postValue(SkuState.SKU_STATE_UNPURCHASED);
                        break;
                    case Purchase.PurchaseState.PURCHASED:
                        if (purchase.isAcknowledged()) {
                            skuStateLiveData.postValue(
                                    SkuState.SKU_STATE_PURCHASED_AND_ACKNOWLEDGED);
                        } else {
                            skuStateLiveData.postValue(SkuState.SKU_STATE_PURCHASED);
                        }
                        break;
                    default:
                        Log.e(TAG, "Purchase in unknown state: " + purchase.getPurchaseState());
                }
            }
        }
    }

    /**
     * Internal call only. Assumes that all signature checks have been completed and the purchase is
     * ready to be consumed. If the sku is already being consumed, does nothing.
     *
     * @param purchase purchase to consume
     */
    private void consumePurchase(@NonNull Purchase purchase) {
        // weak check to make sure we're not already consuming the sku
        if (purchaseConsumptionInProcess.contains(purchase)) {
            // already consuming
            return;
        }
        purchaseConsumptionInProcess.add(purchase);
        mBillingClient.consumeAsync(ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build(), (billingResult, s) -> {
            // ConsumeResponseListener
            purchaseConsumptionInProcess.remove(purchase);
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                Log.d(TAG, "Consumption successful. Delivering entitlement.");
                purchaseConsumed.postValue(purchase.getSkus());
                for (String sku: purchase.getSkus()) {
                    // Since we've consumed the purchase
                    setSkuState(sku, SkuState.SKU_STATE_UNPURCHASED);
                    // And this also qualifies as a new purchase
                }
                newPurchase.postValue(purchase.getSkus());
            } else {
                Log.e(TAG, "Error while consuming: " + billingResult.getDebugMessage());
            }
            Log.d(TAG, "End consumption flow.");
        });
    }

    private enum SkuState {
        SKU_STATE_UNPURCHASED,
        SKU_STATE_PENDING,
        SKU_STATE_PURCHASED,
        SKU_STATE_PURCHASED_AND_ACKNOWLEDGED,
    }

    public void startServiceConnection(final Runnable executeOnSuccess) {
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult  result) {
                Log.d(TAG, "Setup finished. Response code: " + result.getResponseCode());

                if (result.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    mIsServiceConnected = true;
                    if (executeOnSuccess != null) {
                        executeOnSuccess.run();
                    }
                }
                mBillingClientResponseCode = result.getResponseCode();
            }

            @Override
            public void onBillingServiceDisconnected() {
                mIsServiceConnected = false;
            }
        });
    }

    private void executeServiceRequest(Runnable runnable) {
        if (mIsServiceConnected) {
            runnable.run();
        } else {
            // If billing service was disconnected, we try to reconnect 1 time.
            // (feel free to introduce your retry policy here).
            startServiceConnection(runnable);
        }
    }

    /**
     * Verifies that the purchase was signed correctly for this developer's public key.
     * <p>Note: It's strongly recommended to perform such check on your backend since hackers can
     * replace this method with "constant true" if they decompile/rebuild your app.
     * </p>
     */
    private boolean verifyValidSignature(String signedData, String signature) {
        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)

        return Security.verifyPurchase(signedData, signature);

    }
}

