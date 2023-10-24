package com.sesi.chris.animangaquiz.view.fragment;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
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
import java.util.List;

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
    private BroadcastReceiver onDownloadFinishReceiver;
    private  DownloadManager dm;

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
        View viewRoot = inflater.inflate(R.layout.fragment_wallpaper, container, false);
        init(viewRoot);
        return viewRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(View viewRoot){
        Log.i("Wallpaper", "Iniciado");
        context = getContext();
        presenter = new WallpaperPresenter(new WallpaperInteractor(new QuizClient()));
        presenter.setView(this);
        imgBtnBack = viewRoot.findViewById(R.id.imgBtnBack);
        etSearch = viewRoot.findViewById(R.id.et_search);
        etSearch.addTextChangedListener(textWatcherFilter);
        recyclerViewAnimes = viewRoot.findViewById(R.id.recyclerViewAnime);
        recyclerViewWallpapers = viewRoot.findViewById(R.id.recyclerViewWallpaper);
        progressBar = viewRoot.findViewById(R.id.pb_wallpaper);
        user = (User) requireActivity().getIntent().getSerializableExtra("user");
        Log.i("Wallpaper user", user.toString());
        setupRecyclerViewAnimes();
        if (UtilInternetConnection.isOnline(context())){
            if (null != user){
                presenter.getAllAnimes(user.getEmail(),user.getPassword());
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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

            if (((MenuActivity) requireActivity()).getUserActual().getCoins() >= wallpaper.getCosto()){
                costoWalpaper = wallpaper.getCosto();
                String url = Constants.URL_BASE+"/"+ wallpaper.getUrl();
                String formato = url.substring(url.length()-4);
                Log.i("Wallpaper", url);
                Log.i("Wallpaper", formato);
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
            if (((MenuActivity) requireActivity()).getUserActual().getCoins() >= avatar.getCosto()){
                costoWalpaper = avatar.getCosto();
                String sUrl = Constants.URL_BASE + "/" +avatar.getUrl();
                String formato = sUrl.substring(sUrl.length()-4);
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
            presenter.getWallpaperByAnime(user.getEmail(),user.getPassword(),anime.getIdAnime());
        } else {
            Toast.makeText(context(),getString(R.string.noInternet),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void launchAvatarByAnime(Anime anime) {
        if (UtilInternetConnection.isOnline(context())){
            presenter.getAvatarsByAnime(user.getEmail(),user.getPassword(),anime.getIdAnime());
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
        ((MenuActivity) requireActivity()).refreshUserData();

    }

    @Override
    public Context context() {
        return context;
    }

    private void restaGemas(){
        int gemasDisponibles = ((MenuActivity) requireActivity()).getUserActual().getCoins();
        //Update Gemas
        int gemasUpdate = gemasDisponibles - costoWalpaper;
        presenter.updateGemas(user.getEmail(),user.getPassword(),user.getIdUser(),gemasUpdate);
    }

    private void guardarWallpaper(String url, String formato){
        //Descargar la imagen
        downloadWallpaper(url, formato);
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

    private void downloadWallpaper(String url, String format) {
        dm = (DownloadManager) requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
        String date = (DateFormat.format("yyyyMMdd_hhmmss", new java.util.Date()).toString());
        String fname = "Image-" + date + format;

        Long lastDownload = dm.enqueue(new DownloadManager.Request(Uri.parse(url))
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("Download")
                .setDescription("Download Wallpaper")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fname)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED));
        CountDownTimer countDownTimer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                queryStatus(dm, lastDownload);
            }
        }.start();
    }

    public void queryStatus(DownloadManager dm, Long lastDownload) {
        Cursor c= dm.query(new DownloadManager.Query().setFilterById(lastDownload));

        if (c==null) {
            Toast.makeText(getContext(), "Download not found!", Toast.LENGTH_LONG).show();
        }
        else {
            c.moveToFirst();
            Toast.makeText(getContext(), statusMessage(c), Toast.LENGTH_LONG).show();
        }
    }

    private String statusMessage(Cursor c) {
        String msg;

        switch(c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            case DownloadManager.STATUS_FAILED:
                msg="Download failed!";
                break;

            case DownloadManager.STATUS_PAUSED:
                msg="Download paused!";
                break;

            case DownloadManager.STATUS_PENDING:
                msg="Download pending!";
                break;

            case DownloadManager.STATUS_RUNNING:
                msg="Download in progress!";
                break;

            case DownloadManager.STATUS_SUCCESSFUL:
                msg="Download complete!";
                restaGemas();
                break;

            default:
                msg="Download is nowhere in sight";
                break;
        }

        return(msg);
    }
}
