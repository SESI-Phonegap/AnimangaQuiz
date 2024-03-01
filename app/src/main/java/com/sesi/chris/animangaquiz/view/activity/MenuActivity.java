package com.sesi.chris.animangaquiz.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;

import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.material.navigation.NavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.UpdateResponse;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.interactor.LoginInteractor;
import com.sesi.chris.animangaquiz.presenter.LoginPresenter;
import com.sesi.chris.animangaquiz.view.fragment.AnimeCatalogoFragment;
import com.sesi.chris.animangaquiz.view.fragment.BillingPurchaseFragment;
import com.sesi.chris.animangaquiz.view.fragment.FriendsFragment;
import com.sesi.chris.animangaquiz.view.fragment.WallpaperFragment;
import com.sesi.chris.animangaquiz.view.utils.ImageFilePath;
import com.sesi.chris.animangaquiz.view.utils.Utils;
import com.sesi.chris.animangaquiz.view.utils.UtilsPreference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.sesi.chris.animangaquiz.view.utils.UtilInternetConnection.isOnline;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
@AndroidEntryPoint
public class MenuActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoginPresenter.ViewLogin {

    private TextView tvUserName;
    private TextView tvEmail;
    private TextView tvGems;
    private TextView tvTotalScore;
    private CircleImageView imgAvatar;
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
                openGallery()
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
    private final ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    openGallery();
                } else {
                    Toast.makeText(getApplicationContext(), "Se requieren los permisos para continuar", Toast.LENGTH_LONG).show();
                }
            });

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
            loginPresenter.onLogin(userActual.getEmail(), userActual.getPassword());
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


    private void openGallery() {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU){
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        }
        if (ActivityCompat.checkSelfPermission(context(), permission) != PackageManager.PERMISSION_GRANTED) {
            mPermissionResult.launch(permission);
        } else {
            galleryFilter();
        }
    }


    public void galleryFilter() {
        List<Intent> targetGalleryIntents = new ArrayList<>();
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        galleryIntent.setType("image/*");
        //PackageManager pm = getApplicationContext().getPackageManager();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryResult.launch(galleryIntent);
        /*
        List<ResolveInfo> resInfos = pm.queryIntentActivities(galleryIntent, 0);
        if (!resInfos.isEmpty()) {
            for (ResolveInfo resInfo : resInfos) {
                String packageName = resInfo.activityInfo.packageName;

                if (!packageName.contains("com.google.android.apps.photos") && !packageName.equals("com.google.android.apps.plus")) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.putExtra(APP_NAME, resInfo.loadLabel(pm).toString());
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    intent.setPackage(packageName);
                    galleryResult.launch(intent);
                    //targetGalleryIntents.add(intent);
                }
            }

            if (!targetGalleryIntents.isEmpty()) {
                Collections.sort(targetGalleryIntents, (o1, o2) -> o1.getStringExtra(APP_NAME).compareTo(o2.getStringExtra(APP_NAME)));
                Intent chooserIntent = Intent.createChooser(targetGalleryIntents.remove(0), "Abrir Galeria");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetGalleryIntents.toArray(new Parcelable[]{}));
                galleryResult.launch(chooserIntent);
                //startActivityForResult(chooserIntent, PICK_IMAGE);
            } else {
                Toast.makeText(getApplicationContext(), "No se encontro la galeria", Toast.LENGTH_LONG).show();
            }
        }*/
    }

    private final ActivityResultLauncher<Intent> galleryResult = registerForActivityResult( new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    try {
                        Uri imageUri = result.getData().getData();

                        String selectedImagePath;
                        selectedImagePath = ImageFilePath.getPath(getApplication(), imageUri);
                        File fileImage = new File(selectedImagePath);
                        Long lSizeImage = getImageSizeInKb(fileImage.length());

                        if (lSizeImage < 2000) {
                            if (isOnline(context())) {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
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
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            try {
                Uri imageUri = data.getData();

                String selectedImagePath;
                selectedImagePath = ImageFilePath.getPath(getApplication(), imageUri);
                File fileImage = new File(selectedImagePath);
                Long lSizeImage = getImageSizeInKb(fileImage.length());

                if (lSizeImage < 2000) {
                    if (isOnline(context())) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
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
    }
*/
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
        Toast.makeText(context(), updateResponse.estatus + "-" + updateResponse.error, Toast.LENGTH_LONG).show();
        refreshUserData();
    }

    @Override
    public void showUpdateAvatarError() {
        Toast.makeText(context(), R.string.msgAvatrError, Toast.LENGTH_LONG).show();
    }

    @Override
    public void renderResponse(UpdateResponse updateResponse) {
        //Empty Method
    }

    @Override
    public void renderResponseFacebook(UpdateResponse updateResponse) {
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
