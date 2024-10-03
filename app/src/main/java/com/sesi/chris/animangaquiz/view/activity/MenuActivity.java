package com.sesi.chris.animangaquiz.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;

import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.client.QuizClient;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.Credentials;
import com.sesi.chris.animangaquiz.data.dto.InternetDto;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.UpdateResponseD;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.interactor.LoginInteractor;
import com.sesi.chris.animangaquiz.presenter.LoginPresenter;
import com.sesi.chris.animangaquiz.view.fragment.AnimeCatalogoFragment;
import com.sesi.chris.animangaquiz.view.fragment.BillingPurchaseFragment;
import com.sesi.chris.animangaquiz.view.fragment.FriendsFragment;
import com.sesi.chris.animangaquiz.view.fragment.WallpaperFragment;
import com.sesi.chris.animangaquiz.view.utils.ImageFilePath;
import com.sesi.chris.animangaquiz.view.utils.InternetUtil;
import com.sesi.chris.animangaquiz.view.utils.Utils;
import com.sesi.chris.animangaquiz.view.utils.UtilsPreference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import dagger.hilt.android.AndroidEntryPoint;


@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
@AndroidEntryPoint
public class MenuActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoginPresenter.ViewLogin {

    private TextView tvUserName;
    private TextView tvEmail;
    private TextView tvGems;
    private TextView tvTotalScore;
    private ShapeableImageView imgAvatar;
    private ProgressBar progressBar;
    private LoginPresenter loginPresenter;
    private User userActual;
    private Context context;
    private AdView mAdview;
    //private PurchaseFragment fPurchaseFragment;
    private BillingPurchaseFragment billingPurchaseFragment;
    private static final String DIALOG_TAG = "dialog";
    private static final int PICK_IMAGE = 100;
    private ImageView imgShenglong;
    private FrameLayout frameLayout;
    private ImageView imgDragonBalls;
    private static final String APP_NAME = "AppName";

    public User getUserActual() {
        return userActual;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setStatusBarGradiant(this);
        init();
        // Try to restore dialog fragment if we were showing it prior to screen rotation
        if (savedInstanceState != null) {
            billingPurchaseFragment = (BillingPurchaseFragment) getSupportFragmentManager()
                    .findFragmentByTag(DIALOG_TAG);
        }
    }

    public void init() {
        context = this;
        initAds();
        loginPresenter = new LoginPresenter(new LoginInteractor(new QuizClient()));
        loginPresenter.setView(this);
        cargarPublicidad();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        tvEmail = headerView.findViewById(R.id.tv_email);
        tvUserName = headerView.findViewById(R.id.tv_username);
        tvGems = headerView.findViewById(R.id.tv_gemas);
        tvTotalScore = headerView.findViewById(R.id.tv_score);
        imgAvatar = headerView.findViewById(R.id.imgAvatar);
        imgDragonBalls = headerView.findViewById(R.id.imgEsferas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        progressBar = findViewById(R.id.pb_login);
        userActual = (User) getIntent().getSerializableExtra("user");
        changeFragment(AnimeCatalogoFragment.newInstance("quiz"), R.id.mainFrame, false, false);
        imgShenglong = findViewById(R.id.shenglong);
        frameLayout = findViewById(R.id.mainFrame);
        imgDragonBalls.setOnClickListener(v -> {
            if (userActual.getEsferas() == 7) {
                invokeShenlong();
            } else {
                Toast.makeText(context(), R.string.msgErrorShenlong, Toast.LENGTH_LONG).show();
            }
        });

        imgAvatar.setOnClickListener(v ->
                galleryFilter()
        );
    }

    private void initAds(){
        MobileAds.initialize(this, initializationStatus -> { });
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("212A472E4A57221D579423A6E0AC58B2")).build();
        MobileAds.setRequestConfiguration(configuration);
    }


    private void invokeShenlong() {
        loginPresenter.onUpdateGems(userActual.getUserName(),userActual.getPassword(),userActual.getIdUser(),userActual.getCoins() + 10000);
        frameLayout.setVisibility(View.GONE);
        imgShenglong.setVisibility(View.VISIBLE);
        imgShenglong.setBackgroundResource(R.drawable.animation_shenlong);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        AnimationDrawable animationDrawable = (AnimationDrawable) imgShenglong.getBackground();
        animationDrawable.setOneShot(true);
        if (!animationDrawable.isRunning()) {

            int totalFrameDuration = 0;
            for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
                totalFrameDuration += animationDrawable.getDuration(i);
            }
            animationDrawable.start();

            new Handler().postDelayed(animationDrawable::stop, totalFrameDuration);

        }

        loginPresenter.onUpdateEsferas(userActual.getUserName(),userActual.getPassword(),userActual.getIdUser(),0);

        imgShenglong.setOnClickListener(v -> {
            if (!animationDrawable.isRunning()) {
                imgShenglong.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void cargarPublicidad() {
        InternetDto internetDto = InternetUtil.INSTANCE.getConnection(context());
        if (internetDto.isOnline()) {
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
        InternetDto internetDto = InternetUtil.INSTANCE.getConnection(context());
        if (internetDto.isOnline()) {
            Credentials request = new Credentials(userActual.getEmail(), userActual.getPassword());
            loginPresenter.onLogin(request);
        } else {
            Toast.makeText(context(), getString(R.string.noInternet), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUserData();
    }

    @Override
    protected void onDestroy() {
        loginPresenter.terminate();
        super.onDestroy();
    }

    public void changeFragment(Fragment fragment, int resource, boolean isRoot, boolean backStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        refreshUserData();

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


    public void galleryFilter() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        //galleryResult.launch(intent);
        pickMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
    }

    private final // Registers a photo picker activity launcher in single-select mode.
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
        if (uri != null) {
            try {
                String selectedImagePath;
                selectedImagePath = ImageFilePath.getPath(getApplication(), uri);
                File fileImage = new File(selectedImagePath);
                Long lSizeImage = getImageSizeInKb(fileImage.length());

                if (lSizeImage < 2000) {
                    InternetDto internetDto = InternetUtil.INSTANCE.getConnection(context());
                    if (internetDto.isOnline()) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        imgAvatar.setImageBitmap(bitmap);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        String sImgBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        Log.i("BASE64", sImgBase64);
                        //guardar en la BD
                        loginPresenter.onUpdateAvatar(userActual.getEmail(),
                                userActual.getPassword(),
                                userActual.getIdUser(),
                                sImgBase64);

                    } else {
                        Toast.makeText(context(), getString(R.string.noInternet), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context(), R.string.msgImagenError, Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                Log.e("Error-", e.getMessage());
            }
        }
    });

    public Long getImageSizeInKb(Long imageLength) {
        if (imageLength <= 0) {
            return 0L;
        } else {
            return imageLength / 1024;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_quiz:
                changeFragment(AnimeCatalogoFragment.newInstance("quiz"), R.id.mainFrame, false, false);
                break;
            case R.id.nav_quizImg:
                changeFragment(AnimeCatalogoFragment.newInstance("img"), R.id.mainFrame, false, false);
                break;
            case R.id.nav_wallpaper:
                changeFragment(WallpaperFragment.newInstance(), R.id.mainFrame, false, false);
                break;
            case R.id.nav_tienda:
                if (billingPurchaseFragment == null) {
                    billingPurchaseFragment = new BillingPurchaseFragment();
                }
                changeFragment(billingPurchaseFragment, R.id.mainFrame, false, false);

                break;
            case R.id.nav_friend:
                changeFragment(FriendsFragment.newInstance(), R.id.mainFrame, false, false);
                break;
            case R.id.nav_logout:
                UtilsPreference.resetPreferenceUser(context);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(intent);
                break;
            case R.id.nav_delete:
                showConfirmDialog();
                break;
            default:
                Utils.sharedSocial(context(), userActual.getUserName());
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showConfirmDialog(){
        AlertDialog dialog;
        AlertDialog.Builder builder =  new AlertDialog.Builder(context());
        final View view = getLayoutInflater().inflate(R.layout.dialog_confirmar, null);

        TextView tvMensaje = view.findViewById(R.id.tv_mensaje);
        Button btnAceptar = view.findViewById(R.id.btn_aceptar);
        Button btnCancel = view.findViewById(R.id.btn_cancel);

        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        tvMensaje.setText(context().getString(R.string.dialog_delete_msg));

        btnAceptar.setOnClickListener(v -> {
            loginPresenter.deleteUser(userActual.getEmail(), userActual.getPassword(), userActual.getIdUser());
            dialog.dismiss();
        });

        btnCancel.setOnClickListener((View v) -> dialog.dismiss());

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
            String sName = user.getName();
            String sEmail = user.getEmail();
            tvUserName.setText((sName != null) ? sName : user.getName());
            tvEmail.setText((sEmail != null) ? sEmail : user.getEmail());
            tvTotalScore.setText(String.format(getString(R.string.score), user.getTotalScore()));
            tvGems.setText(String.valueOf(user.getCoins()));
            switch (user.getEsferas()) {
                case 1:
                    imgDragonBalls.setVisibility(View.VISIBLE);
                    imgDragonBalls.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.esferas1));
                    break;
                case 2:
                    imgDragonBalls.setVisibility(View.VISIBLE);
                    imgDragonBalls.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.esferas2));
                    break;
                case 3:
                    imgDragonBalls.setVisibility(View.VISIBLE);
                    imgDragonBalls.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.esferas3));
                    break;
                case 4:
                    imgDragonBalls.setVisibility(View.VISIBLE);
                    imgDragonBalls.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.esferas4));
                    break;
                case 5:
                    imgDragonBalls.setVisibility(View.VISIBLE);
                    imgDragonBalls.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.esferas5));
                    break;
                case 6:
                    imgDragonBalls.setVisibility(View.VISIBLE);
                    imgDragonBalls.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.esferas6));
                    break;
                case 7:
                    imgDragonBalls.setVisibility(View.VISIBLE);
                    imgDragonBalls.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.esferas7));
                    break;
                default:
                    imgDragonBalls.setVisibility(View.INVISIBLE);
                    break;
            }
            userActual = user;
            if (!user.getUrlImageUser().isEmpty()) {
                byte[] decodedAvatar = Base64.decode(user.getUrlImageUser(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedAvatar, 0, decodedAvatar.length);
                imgAvatar.setImageBitmap(decodedByte);
            } else {
                imgAvatar.setImageResource(R.drawable.ic_account_circle_white_48dp);
            }

        }
    }

    @Override
    public void updateGemsResponse(UpdateResponseD updateResponse) {
        Toast.makeText(context(), updateResponse.estatus + "-" + updateResponse.error, Toast.LENGTH_LONG).show();
        refreshUserData();
    }

    @Override
    public void showUpdateGemsError() {
        Toast.makeText(context(), R.string.updateGemsError, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateAvatarResponse(UpdateResponseD updateResponse) {
        Toast.makeText(context(), updateResponse.estatus + "-" + updateResponse.error, Toast.LENGTH_LONG).show();
        refreshUserData();
    }

    @Override
    public void showUpdateAvatarError() {
        Toast.makeText(context(), R.string.msgAvatrError, Toast.LENGTH_LONG).show();
    }

    @Override
    public void renderResponse(UpdateResponseD updateResponse) {
        //Empty Method
    }

    @Override
    public void renderResponseFacebook(UpdateResponseD updateResponse) {
        //Empty Method
    }

    @Override
    public void renderLoginFacbook(LoginResponse loginResponse) {
        //Empty Method
    }

    @Override
    public void deleteUserAction() {
        UtilsPreference.deleteAll(context());
        startActivity(new Intent(context(), LoginActivity.class));
        finish();
    }

    @Override
    public Context context() {
        return context;
    }

}
