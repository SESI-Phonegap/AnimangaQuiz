package com.sesi.chris.animangaquiz.view.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.client.QuizClient;
import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.data.model.Wallpaper;
import com.sesi.chris.animangaquiz.interactor.WallpaperInteractor;
import com.sesi.chris.animangaquiz.presenter.WallpaperPresenter;
import com.sesi.chris.animangaquiz.view.adapter.AnimeAdapter;
import com.sesi.chris.animangaquiz.view.adapter.WallpaperAdapter;
import com.sesi.chris.animangaquiz.view.utils.UtilInternetConnection;
import com.sesi.chris.animangaquiz.view.utils.Utils;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WallpaperFragment extends Fragment implements WallpaperPresenter.View {

    private Context context;
    private WallpaperPresenter presenter;
    private TextView et_Search;
    private ImageView imgBtnBack;
    private RecyclerView recyclerViewAnimes;
    private RecyclerView recyclerViewWallpapers;
    private ProgressBar progressBar;
    private List<Anime> lstAnime;
    private List<Wallpaper> lstWallpaper;
    private User user;
    private ConstraintLayout constraintLayoutSearch;
    private RelativeLayout relativeGemas;
    private TextView tvGemasUser;

    public WallpaperFragment() {
        // Required empty public constructor
    }

    public static WallpaperFragment newInstance() {
        WallpaperFragment fragment = new WallpaperFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
        relativeGemas = getActivity().findViewById(R.id.relativeGemas);
        tvGemasUser = getActivity().findViewById(R.id.tv_gemas_user);
        imgBtnBack = getActivity().findViewById(R.id.imgBtnBack);
        et_Search = Objects.requireNonNull(getActivity()).findViewById(R.id.et_search);
        et_Search.addTextChangedListener(textWatcherFilter);
        recyclerViewAnimes = getActivity().findViewById(R.id.recyclerViewAnime);
        recyclerViewWallpapers = getActivity().findViewById(R.id.recyclerViewWallpaper);
        progressBar = getActivity().findViewById(R.id.pb_wallpaper);
        constraintLayoutSearch = getActivity().findViewById(R.id.constraintSearch);
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
        et_Search.setVisibility(View.GONE);
        imgBtnBack.setVisibility(View.VISIBLE);
        relativeGemas.setVisibility(View.VISIBLE);
        tvGemasUser.setText(String.valueOf(user.getCoins()));
    }

    private void changeUiAnimeList(){
        recyclerViewAnimes.setVisibility(View.VISIBLE);
        recyclerViewWallpapers.setVisibility(View.GONE);
        et_Search.setVisibility(View.VISIBLE);
        imgBtnBack.setVisibility(View.GONE);
        relativeGemas.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setupRecyclerViewAnimes(){
        AnimeAdapter adapter = new AnimeAdapter();
        adapter.setItemClickListener((Anime anime) -> presenter.launchWallpaperAnime(anime));
        recyclerViewAnimes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scrolling up
                    constraintLayoutSearch.setVisibility(View.GONE);
                } else {
                    // Scrolling down
                    constraintLayoutSearch.setVisibility(View.VISIBLE);
                }
            }
        });
        recyclerViewAnimes.setAdapter(adapter);
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
            if (user.getCoins() >= wallpaper.getCosto()){
                String url = wallpaper.getUrl();
                String formato = url.substring(url.length()-4,url.length());
                //Descargar la imagen
                DownloadWallpaperTask download = new DownloadWallpaperTask();
                download.execute(wallpaper.getUrl(),formato);
            } else {
                Toast.makeText(context(),getString(R.string.noAlcanza),Toast.LENGTH_LONG).show();
            }
        });
        adapter.notifyDataSetChanged();
        recyclerViewWallpapers.setAdapter(adapter);
        this.lstWallpaper = lstWallpaper;
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
    public Context context() {
        return context;
    }

    TextWatcher textWatcherFilter = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //EMPTY
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //AnimeAdapter adapter = (AnimeAdapter) recyclerViewAnimes.getAdapter();
            List<Anime> lstAnimeFilter = new ArrayList<>();
            for (Anime anime : lstAnime){
                String textAnime = anime.getAnime().toLowerCase();
                if (textAnime.contains(s)){
                    lstAnimeFilter.add(anime);
                }
            }
            AnimeAdapter adapterFilter = new AnimeAdapter();
            adapterFilter.setItemClickListener((Anime anime) -> presenter.launchWallpaperAnime(anime));
            adapterFilter.setLstAnimes(lstAnimeFilter);
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
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            Bitmap bitmap = null;
            try {
                sFormato = url[1];
                InputStream inputStream = new URL(url[0]).openStream();   // Download Image from URL
                bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (Utils.isExternalStorageWritable()) {
                if (Utils.SaveImage(bitmap, sFormato, context())) {
                    //Descontar Gemas

                }
            } else {
                Toast.makeText(context(),getString(R.string.msgNoSd),Toast.LENGTH_LONG).show();
            }
        }
    }
}
