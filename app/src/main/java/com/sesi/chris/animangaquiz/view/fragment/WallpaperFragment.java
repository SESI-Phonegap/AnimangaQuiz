package com.sesi.chris.animangaquiz.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.Constants;
import com.sesi.chris.animangaquiz.data.api.client.QuizClient;
import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.UpdateResponse;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.data.model.Wallpaper;
import com.sesi.chris.animangaquiz.interactor.WallpaperInteractor;
import com.sesi.chris.animangaquiz.presenter.WallpaperPresenter;
import com.sesi.chris.animangaquiz.view.activity.MenuActivity;
import com.sesi.chris.animangaquiz.view.adapter.AnimeAdapter;
import com.sesi.chris.animangaquiz.view.adapter.WallpaperAdapter;
import com.sesi.chris.animangaquiz.view.utils.UtilInternetConnection;
import com.sesi.chris.animangaquiz.view.utils.Utils;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WallpaperFragment extends Fragment implements WallpaperPresenter.ViewWallpaper {

    private Context context;
    private WallpaperPresenter presenter;
    private TextView etSearch;
    private ImageView imgBtnBack;
    private RecyclerView recyclerViewAnimes;
    private RecyclerView recyclerViewWallpapers;
    private ProgressBar progressBar;
    private List<Anime> lstAnime;
    private User user;
    private int costoWalpaper;

    public WallpaperFragment() {
        // Required empty public constructor
    }

    public static WallpaperFragment newInstance() {
        return new WallpaperFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallpaper, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init(){
        context = getContext();
        presenter = new WallpaperPresenter(new WallpaperInteractor(new QuizClient()));
        presenter.setView(this);
        imgBtnBack = Objects.requireNonNull(getActivity()).findViewById(R.id.imgBtnBack);
        etSearch = Objects.requireNonNull(getActivity()).findViewById(R.id.et_search);
        etSearch.addTextChangedListener(textWatcherFilter);
        recyclerViewAnimes = getActivity().findViewById(R.id.recyclerViewAnime);
        recyclerViewWallpapers = getActivity().findViewById(R.id.recyclerViewWallpaper);
        progressBar = getActivity().findViewById(R.id.pb_wallpaper);
        user = (User) getActivity().getIntent().getSerializableExtra("user");
        setupRecyclerViewAnimes();
        if (UtilInternetConnection.isOnline(context())){
            if (null != user){
                presenter.getAllAnimes(user.getUserName(),user.getPassword());
            } else {
                Toast.makeText(context(),"Ocurrio un error",Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context(),getString(R.string.noInternet),Toast.LENGTH_LONG).show();
        }

        imgBtnBack.setOnClickListener(v -> changeUiAnimeList());
    }

    private void changeUiWallpaper(){
        recyclerViewAnimes.setVisibility(View.GONE);
        recyclerViewWallpapers.setVisibility(View.VISIBLE);
        etSearch.setVisibility(View.GONE);
        imgBtnBack.setVisibility(View.VISIBLE);
    }

    private void changeUiAnimeList(){
        recyclerViewAnimes.setVisibility(View.VISIBLE);
        recyclerViewWallpapers.setVisibility(View.GONE);
        etSearch.setVisibility(View.VISIBLE);
        imgBtnBack.setVisibility(View.GONE);
    }

    private void setupRecyclerViewAnimes(){
        AnimeAdapter adapter = new AnimeAdapter();
        adapter.setItemClickListener(this::showWallpaperAvatarDialog);
        recyclerViewAnimes.setAdapter(adapter);
    }

    public void showWallpaperAvatarDialog(Anime anime){
        AlertDialog dialog;
        AlertDialog.Builder builder =  new AlertDialog.Builder(context());
        final View view = getLayoutInflater().inflate(R.layout.dialog_confirmar, null);
        TextView tvMensaje = view.findViewById(R.id.tv_mensaje);
        Button btnWallpapers = view.findViewById(R.id.btn_aceptar);
        Button btnAvatars = view.findViewById(R.id.btn_cancel);
        btnWallpapers.setText(getString(R.string.wallpapers));
        btnAvatars.setText(getString(R.string.avatars));
        btnAvatars.setVisibility(View.GONE);
        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        tvMensaje.setText(context().getString(R.string.msg_wallpapers));

        btnWallpapers.setOnClickListener((View v) -> {
            presenter.launchWallpaperAnime(anime);
            dialog.dismiss();
        });

        btnAvatars.setOnClickListener((View v) -> {
            presenter.launchAvatarByAnime(anime);
            dialog.dismiss();
        });
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
    public void showAnimesNotFoundMessage() {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(context(),getString(R.string.sinDatos),Toast.LENGTH_LONG).show();
    }

    @Override
    public void showServerError(String error) {
        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(context(),error,Toast.LENGTH_LONG).show();
    }

    @Override
    public void renderAnimes(List<Anime> lstAnimes) {
        AnimeAdapter adapter = (AnimeAdapter) recyclerViewAnimes.getAdapter();
        adapter.setLstAnimes(lstAnimes);
        adapter.notifyDataSetChanged();
        this.lstAnime = lstAnimes;
    }

    @Override
    public void renderWallpaperByAnimes(List<Wallpaper> lstWallpaper) {
        changeUiWallpaper();
        WallpaperAdapter adapter = new WallpaperAdapter();
        adapter.setLstWallpaper(lstWallpaper);
        adapter.setItemClickListener((Wallpaper wallpaper) -> {

            if (((MenuActivity) Objects.requireNonNull(getActivity())).getUserActual().getCoins() >= wallpaper.getCosto()){
                costoWalpaper = wallpaper.getCosto();
                String url = Constants.URL_BASE + wallpaper.getUrl();
                String formato = url.substring(url.length()-4,url.length());
                showConfirmDialog(costoWalpaper, url,formato);

            } else {
                Toast.makeText(context(),getString(R.string.noAlcanza),Toast.LENGTH_LONG).show();
            }
        });
        adapter.notifyDataSetChanged();
        recyclerViewWallpapers.setAdapter(adapter);
    }

    @Override
    public void renderAvatarsByAnime(List<Wallpaper> lstAvatar) {
        changeUiWallpaper();
        WallpaperAdapter adapter = new WallpaperAdapter();
        adapter.setLstWallpaper(lstAvatar);
        adapter.setItemClickListener((Wallpaper avatar) -> {
            if (((MenuActivity) Objects.requireNonNull(getActivity())).getUserActual().getCoins() >= avatar.getCosto()){
                costoWalpaper = avatar.getCosto();
                String sUrl = Constants.URL_BASE + avatar.getUrl();
                String formato = sUrl.substring(sUrl.length()-4,sUrl.length());
                showConfirmDialog(costoWalpaper,sUrl,formato);
            } else {
                Toast.makeText(context(),getString(R.string.noAlcanza),Toast.LENGTH_LONG).show();
            }
        });
        adapter.notifyDataSetChanged();
        recyclerViewWallpapers.setAdapter(adapter);
    }

    @Override
    public void launchWallpaperByanime(Anime anime) {
        if (UtilInternetConnection.isOnline(context())){
            presenter.getWallpaperByAnime(user.getUserName(),user.getPassword(),anime.getIdAnime());
        } else {
            Toast.makeText(context(),getString(R.string.noInternet),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void launchAvatarByAnime(Anime anime) {
        if (UtilInternetConnection.isOnline(context())){
            presenter.getAvatarsByAnime(user.getUserName(),user.getPassword(),anime.getIdAnime());
        } else {
            Toast.makeText(context(),getString(R.string.noInternet),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void renderUpdateGemas(UpdateResponse updateResponse) {
        if (null != updateResponse){
            Log.d("GEMASRESPONSE--",updateResponse.estatus);
            Log.d("GEMASRESPONSE--",updateResponse.error);
        }
        ((MenuActivity) Objects.requireNonNull(getActivity())).refreshUserData();

    }

    @Override
    public Context context() {
        return context;
    }

    private void restaGemas(){
        int gemasDisponibles = ((MenuActivity) Objects.requireNonNull(getActivity())).getUserActual().getCoins();
        //Update Gemas
        int gemasUpdate = gemasDisponibles - costoWalpaper;
        presenter.updateGemas(user.getUserName(),user.getPassword(),user.getIdUser(),gemasUpdate);
    }

    private void guardarWallpaper(String url, String formato){
        //Descargar la imagen
        DownloadWallpaperTask download = new DownloadWallpaperTask();
        download.execute(url,formato);
    }

    public void showConfirmDialog(int costoWallpaper, String url, String formato){
        AlertDialog dialog;
        AlertDialog.Builder builder =  new AlertDialog.Builder(context());
        final View view = this.getLayoutInflater().inflate(R.layout.dialog_confirmar, null);

        TextView tvMensaje = view.findViewById(R.id.tv_mensaje);
        Button btnAceptar = view.findViewById(R.id.btn_aceptar);
        Button btnCancel = view.findViewById(R.id.btn_cancel);

        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        tvMensaje.setText(context().getString(R.string.msg_confirmacion,String.valueOf(costoWallpaper)));

        btnAceptar.setOnClickListener(v -> {
            guardarWallpaper(url,formato);
            dialog.dismiss();
        });

        btnCancel.setOnClickListener((View v) -> dialog.dismiss());

    }

    TextWatcher textWatcherFilter = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //EMPTY
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            AnimeAdapter adapterFilter = new AnimeAdapter();
            adapterFilter.setItemClickListener((Anime anime) -> showWallpaperAvatarDialog(anime));
            adapterFilter.setLstAnimes(Utils.filtrarAnime(lstAnime,s));
            recyclerViewAnimes.setAdapter(adapterFilter);
            adapterFilter.notifyDataSetChanged();
        }

        @Override
        public void afterTextChanged(Editable s) {
            //EMPTY
        }
    };

    private class DownloadWallpaperTask extends AsyncTask<String,Integer,Bitmap> {

        private String sFormato;

        @Override
        protected Bitmap doInBackground(String... url) {
            Bitmap bitmap = null;
            try {
                sFormato = url[1];
                InputStream inputStream = new URL(url[0]).openStream();   // Download Image from URL
                bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
                inputStream.close();
            } catch (Exception e) {
                Log.e("Error-",e.getMessage());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (Utils.isExternalStorageWritable()) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 999);
                    } else {
                        if (Utils.saveImage(bitmap, sFormato, context())) {
                            //Descontar Gemas
                            restaGemas();
                            Toast.makeText(context(),R.string.msgWallpaperSaved2,Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    if (Utils.saveImage(bitmap, sFormato, context())) {
                        //Descontar Gemas
                        restaGemas();
                        Toast.makeText(context(),R.string.msgWallpaperSaved2,Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(context(),getString(R.string.msgNoSd),Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
           Toast.makeText(context(),"Permiso concedido, vuelve a descargar la imagen",Toast.LENGTH_LONG).show();
        }
    }
}
