package com.sesi.chris.animangaquiz.view.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.Constants;
import com.sesi.chris.animangaquiz.data.api.billing.BillingManager;
import com.sesi.chris.animangaquiz.data.api.billing.BillingProvider;
import com.sesi.chris.animangaquiz.data.api.client.QuizClient;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.UpdateResponse;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.interactor.LoginInteractor;
import com.sesi.chris.animangaquiz.presenter.LoginPresenter;
import com.sesi.chris.animangaquiz.view.adapter.LargeGemsDelegate;
import com.sesi.chris.animangaquiz.view.adapter.MedGemsDelegate;
import com.sesi.chris.animangaquiz.view.adapter.SmallGemsDelegate;
import com.sesi.chris.animangaquiz.view.fragment.AnimeCatalogoFragment;
import com.sesi.chris.animangaquiz.view.fragment.FriendsFragment;
import com.sesi.chris.animangaquiz.view.fragment.PurchaseFragment;
import com.sesi.chris.animangaquiz.view.fragment.WallpaperFragment;
import com.sesi.chris.animangaquiz.view.utils.ImageFilePath;
import com.sesi.chris.animangaquiz.view.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;

import static com.sesi.chris.animangaquiz.data.api.billing.BillingManager.BILLING_MANAGER_NOT_INITIALIZED;
import static com.sesi.chris.animangaquiz.view.utils.UtilInternetConnection.isOnline;

public class MenuActivity extends AppCompatActivity
        implements BillingProvider, NavigationView.OnNavigationItemSelectedListener, LoginPresenter.View {

    private TextView tv_userName;
    private TextView tv_email;
    private TextView tv_gems;
    private TextView tv_totalScore;
    private CircleImageView imgAvatar;
    private ProgressBar progressBar;
    private LoginPresenter loginPresenter;
    public User userActual;
    private Context context;
    private AdView mAdview;
    private PurchaseFragment fPurchaseFragment;
    private static final String DIALOG_TAG = "dialog";
    private BillingManager mBillingManager;
    private AlertDialog dialog;
    private static final int PICK_IMAGE = 100;
    private ExifInterface exifInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        init();
        // Try to restore dialog fragment if we were showing it prior to screen rotation
        if (savedInstanceState != null) {
            fPurchaseFragment = (PurchaseFragment) getSupportFragmentManager()
                    .findFragmentByTag(DIALOG_TAG);
        }
    }

    public void init() {
        UpdateListener mUpdateListener = new UpdateListener();
        // Create and initialize BillingManager which talks to BillingLibrary
        mBillingManager = new BillingManager(this, mUpdateListener);
        context = this;
        MobileAds.initialize(this, getString(R.string.admodId));
        loginPresenter = new LoginPresenter(new LoginInteractor(new QuizClient()));
        loginPresenter.setView(this);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        tv_email = headerView.findViewById(R.id.tv_email);
        tv_userName = headerView.findViewById(R.id.tv_username);
        tv_gems = headerView.findViewById(R.id.tv_gemas);
        tv_totalScore = headerView.findViewById(R.id.tv_score);
        imgAvatar = headerView.findViewById(R.id.imgAvatar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        progressBar = findViewById(R.id.pb_login);
        userActual = (User) getIntent().getSerializableExtra("user");
        refreshUserData();
        changeFragment(AnimeCatalogoFragment.newInstance(), R.id.mainFrame, false, false);
        cargarPublicidad();

        imgAvatar.setOnClickListener(v -> {
            openGallery();
        });

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(context(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 999);
            }
        }
    }

    private void cargarPublicidad() {
        if (isOnline(context())) {
            mAdview = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdview.loadAd(adRequest);
            mAdview.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    // Load the next interstitial.
                    mAdview.loadAd(new AdRequest.Builder().build());
                }
            });

        } else {
            ImageView imgPubli = findViewById(R.id.img_publi_no_internet);
            imgPubli.setVisibility(View.VISIBLE);
        }
    }

    public void refreshUserData() {
        if (isOnline(context())) {
            loginPresenter.onLogin(userActual.getUserName(), userActual.getPassword());
        } else {
            Toast.makeText(context(), getString(R.string.noInternet), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  refreshUserData();
    }

    @Override
    protected void onDestroy() {
        loginPresenter.terminate();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    public void changeFragment(Fragment fragment, int resource, boolean isRoot, boolean backStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (isRoot) {
            transaction.add(resource, fragment);
        } else {
            transaction.replace(resource, fragment);
        }

        if (backStack) {
            transaction.addToBackStack(null);
        }
        //  transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.enter_from_left);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    /**
     * Remove loading spinner and refresh the UI
     */
    public void showRefreshedUi() {
        // setWaitScreen(false);
        // updateUi();
        if (fPurchaseFragment != null) {
            fPurchaseFragment.refreshUI();
        }
    }

    /**
     * Update UI to reflect model
     */
 /*   @UiThread
    private void updateUi() {
        Log.d(TAG, "Updating the UI. Thread: " + Thread.currentThread().getName());

        if (isSixMonthlySubscribed()) {
            imageViewIcon.setImageDrawable(this.getResources().getDrawable(R.drawable.coin_month));
        } else if (isYearlySubscribed()){
            imageViewIcon.setImageDrawable(this.getResources().getDrawable(R.drawable.coin_year));
        }
    }*/
    private void openGallery() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(context(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MenuActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 999);

            } else {
                /*Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);*/
                galleryFilter();
            }
        } else {
            /*Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);*/
            galleryFilter();
        }

    }

    public void galleryFilter() {
        List<Intent> targetGalleryIntents = new ArrayList<>();
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        PackageManager pm = getApplicationContext().getPackageManager();
        List<ResolveInfo> resInfos = pm.queryIntentActivities(galleryIntent, 0);
        if (!resInfos.isEmpty()) {
            for (ResolveInfo resInfo : resInfos) {
                String packageName = resInfo.activityInfo.packageName;

                if (!packageName.contains("com.google.android.apps.photos") && !packageName.equals("com.google.android.apps.plus")) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.putExtra("AppName", resInfo.loadLabel(pm).toString());
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    intent.setPackage(packageName);
                    targetGalleryIntents.add(intent);
                }
            }

            if (!targetGalleryIntents.isEmpty()) {
                Collections.sort(targetGalleryIntents, new Comparator<Intent>() {
                    @Override
                    public int compare(Intent o1, Intent o2) {
                        return o1.getStringExtra("AppName").compareTo(o2.getStringExtra("AppName"));
                    }
                });
                Intent chooserIntent = Intent.createChooser(targetGalleryIntents.remove(0), "Abrir Galeria");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetGalleryIntents.toArray(new Parcelable[]{}));
                startActivityForResult(chooserIntent, PICK_IMAGE);
            } else {
                Toast.makeText(getApplicationContext(), "No se encontro la galeria", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            try {
                Uri imageUri = data.getData();

                String selectedImagePath;
                if (Build.VERSION.SDK_INT >= 23) {
                    selectedImagePath = ImageFilePath.getPath(getApplication(), imageUri);
                } else {
                    selectedImagePath = imageUri.toString();
                }

                exifInterface = new ExifInterface(selectedImagePath);
                File fileImage = new File(selectedImagePath);
                Long lSizeImage = getImageSizeInKb(fileImage.length());

                if (lSizeImage < 2000) {
                    if (isOnline(context())) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        imgAvatar.setImageBitmap(bitmap);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                        byte[] imageBytes = baos.toByteArray();
                        String sImgBase64 = Base64.encodeToString(imageBytes,Base64.DEFAULT);
                        Log.i("BASE64",sImgBase64);
                        //guardar en la BD
                        loginPresenter.onUpdateAvatar(userActual.getUserName(),
                                userActual.getPassword(),
                                userActual.getIdUser(),
                                sImgBase64);

                    } else {
                        Toast.makeText(context(), getString(R.string.noInternet), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context(),R.string.msgImagenError,Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Long getImageSizeInKb(Long imageLength) {
        if (imageLength <= 0) {
            return 0l;
        } else {
            return imageLength / 1024;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_quiz) {
            // Handle the camera action
            changeFragment(AnimeCatalogoFragment.newInstance(), R.id.mainFrame, false, false);
        } else if (id == R.id.nav_wallpaper) {
            changeFragment(WallpaperFragment.newInstance(), R.id.mainFrame, false, false);
        } else if (id == R.id.nav_tienda) {
            if (fPurchaseFragment == null) {
                fPurchaseFragment = new PurchaseFragment();
            }
            if (mBillingManager != null
                    && mBillingManager.getBillingClientResponseCode()
                    > BILLING_MANAGER_NOT_INITIALIZED) {
                //mAcquireFragment.onManagerReady(this);
                fPurchaseFragment.onManagerReady(this);
                // createDialogCompras();
                changeFragment(fPurchaseFragment, R.id.mainFrame, false, false);
            }
        } else if (id == R.id.nav_compartit) {
            Utils.sharedSocial(context(),userActual.getUserName());
        } else if (id == R.id.nav_friend) {
            changeFragment(FriendsFragment.newInstance(), R.id.mainFrame, false, false);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showLoginNotFoundMessage() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showServerError(String error) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(context(), getString(R.string.serverError, error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void renderLogin(LoginResponse loginResponse) {
        User user = loginResponse.getUser();
        if (null != user) {
            tv_userName.setText(user.getUserName());
            tv_email.setText(user.getEmail());
            tv_totalScore.setText(getString(R.string.score, String.valueOf(user.getTotalScore())));
            tv_gems.setText(String.valueOf(user.getCoins()));
            userActual = user;
            if (user.getUrlImageUser() != "") {
                byte[] decodedAvatar = Base64.decode(user.getUrlImageUser(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedAvatar, 0, decodedAvatar.length);
                imgAvatar.setImageBitmap(decodedByte);
            } else {
                imgAvatar.setImageResource(R.drawable.ic_account_circle_white_48dp);
            }

        }
    }

    @Override
    public void updateGemsResponse(UpdateResponse updateResponse) {
        Toast.makeText(context(), updateResponse.estatus + "-" + updateResponse.error, Toast.LENGTH_LONG).show();
        refreshUserData();
    }

    @Override
    public void showUpdateGemsError() {
        Toast.makeText(context(), R.string.updateGemsError, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateAvatarResponse(UpdateResponse updateResponse) {
        Toast.makeText(context(),updateResponse.estatus+"-"+updateResponse.error,Toast.LENGTH_LONG).show();
        refreshUserData();
    }

    @Override
    public void showUpdateAvatarError() {
        Toast.makeText(context(),R.string.msgAvatrError,Toast.LENGTH_LONG).show();
    }

    @Override
    public Context context() {
        return context;
    }

    @Override
    public BillingManager getBillingManager() {
        return mBillingManager;
    }

    @Override
    public boolean isSixMonthlySubscribed() {
        return false;
    }

    @Override
    public boolean isYearlySubscribed() {
        return false;
    }


    /**
     * Handler to billing updates
     */
    private class UpdateListener implements BillingManager.BillingUpdatesListener {
        @Override
        public void onBillingClientSetupFinished() {
            if (null != fPurchaseFragment)
                fPurchaseFragment.onManagerReady(MenuActivity.this);
        }

        @Override
        public void onConsumeFinished(String token, @BillingClient.BillingResponse int result) {
            Log.d("TAG", "Consumption finished. Purchase token: " + token + ", result: " + result);

            // Note: We know this is the SKU_GAS, because it's the only one we consume, so we don't
            // check if token corresponding to the expected sku was consumed.
            // If you have more than one sku, you probably need to validate that the token matches
            // the SKU you expect.
            // It could be done by maintaining a map (updating it every time you call consumeAsync)
            // of all tokens into SKUs which were scheduled to be consumed and then looking through
            // it here to check which SKU corresponds to a consumed token.
//---------------------------------------------------------
            if (result == BillingClient.BillingResponse.OK) {
                Log.d("TAG", "Consumption successful. Provisioning.");
                int iGemas = 0;
                switch (token) {
                    case SmallGemsDelegate.SKU_ID:
                        iGemas = userActual.getCoins() + Constants.Compras.SMALL_GEMS;
                        loginPresenter.onUpdateGems(userActual.getUserName(), userActual.getPassword(), userActual.getIdUser(), iGemas);
                        break;

                    case MedGemsDelegate.SKU_ID:
                        iGemas = userActual.getCoins() + Constants.Compras.MED_GEMS;
                        loginPresenter.onUpdateGems(userActual.getUserName(), userActual.getPassword(), userActual.getIdUser(), iGemas);
                        break;

                    case LargeGemsDelegate.SKU_ID:
                        iGemas = userActual.getCoins() + Constants.Compras.LARGE_GEMS;
                        loginPresenter.onUpdateGems(userActual.getUserName(), userActual.getPassword(), userActual.getIdUser(), iGemas);
                        break;
                }
            } else {
                Toast.makeText(context(), R.string.errorCompra, Toast.LENGTH_LONG).show();
                Log.d("TAG", "Error consumption");
            }

            showRefreshedUi();
            Log.d("TAG", "End consumption flow.");
        }

        @Override
        public void onPurchasesUpdated(List<Purchase> purchaseList) {
      /*      mGoldMonthly = false;
            mGoldYearly = false;

            for (Purchase purchase : purchaseList) {
                Log.d("SUSCRIPCION",purchase.getSku());
                switch (purchase.getSku()) {

                    case MONTH_SKU:
                        mGoldMonthly = true;
                        mIsSuscrip = true;
                        break;
                    case ANO_SKU:
                        mGoldYearly = true;
                        mIsSuscrip = true;
                        break;
                }
            }

            if (!mIsSuscrip) {
                Log.d("FALLO","es true");
                cargaPublicidad();
            }
*/
            // mActivity.showRefreshedUi();
        }
    }
}
