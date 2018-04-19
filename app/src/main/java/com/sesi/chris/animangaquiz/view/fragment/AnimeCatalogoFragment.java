package com.sesi.chris.animangaquiz.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.client.QuizClient;
import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.Score;
import com.sesi.chris.animangaquiz.data.model.ScoreResponse;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.interactor.MenuInteractor;
import com.sesi.chris.animangaquiz.presenter.MenuPresenter;
import com.sesi.chris.animangaquiz.view.activity.LoginActivity;
import com.sesi.chris.animangaquiz.view.activity.PreguntasActivity;
import com.sesi.chris.animangaquiz.view.adapter.AnimeAdapter;
import com.sesi.chris.animangaquiz.view.utils.UtilInternetConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnimeCatalogoFragment extends Fragment implements MenuPresenter.View {

    private MenuPresenter menuPresenter;
    private RecyclerView recyclerViewAnimes;
    private ProgressBar progressBar;
    private Context context;
    private AlertDialog dialog;
    private User user;
    private ConstraintLayout constraintLayoutSearch;
    private List<Anime> lstAnime;
    private int iLevel;
    private int iScore;
    private int idAnime;

    public AnimeCatalogoFragment() {
        // Required empty public constructor
    }

    public static AnimeCatalogoFragment newInstance() {
        return new AnimeCatalogoFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anime_catalogo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    public void init(){
        context = getContext();
        menuPresenter = new MenuPresenter(new MenuInteractor(new QuizClient()));
        menuPresenter.setView(this);

        TextView et_Search = Objects.requireNonNull(getActivity()).findViewById(R.id.et_search);
        et_Search.addTextChangedListener(textWatcherFilter);
        constraintLayoutSearch = getActivity().findViewById(R.id.constraintSearch);
        recyclerViewAnimes = getActivity().findViewById(R.id.recyclerViewAnime);
        progressBar = getActivity().findViewById(R.id.pb_login);
        user = (User) getActivity().getIntent().getSerializableExtra("user");
        setupRecyclerView();
        if (UtilInternetConnection.isOnline(context())){
            if (null != user) {
                menuPresenter.getAllAnimes(user.getUserName(), user.getPassword());
            } else {
                Toast.makeText(context(),"Ocurrio un error",Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context(),getString(R.string.noInternet),Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context(),LoginActivity.class);
            startActivity(intent);
        }
    }

    private void setupRecyclerView(){
        AnimeAdapter adapter = new AnimeAdapter();
        adapter.setItemClickListener((Anime anime) -> menuPresenter.launchAnimeTest(anime));
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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        menuPresenter.terminate();
        super.onDestroy();
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
    public void renderScoreAndLevel(ScoreResponse scoreResponse) {
        Score score = scoreResponse.getScore();
        if (null != score) {
            iLevel = Integer.parseInt(score.getLevel());
            iScore = Integer.parseInt(score.getScore());
            createDialogLevel(user);
        } else {
            Toast.makeText(context(),scoreResponse.getError(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showScoreError() {
        Toast.makeText(context(),"Error !!!!",Toast.LENGTH_LONG).show();
    }

    @Override
    public void launchAnimeTest(Anime anime) {
        if (UtilInternetConnection.isOnline(context())){
            idAnime = anime.getIdAnime();
            menuPresenter.checkScoreAndLevel(user.getUserName(),user.getPassword(),anime.getIdAnime(),user.getIdUser());
        } else {
            Toast.makeText(context(),getString(R.string.noInternet),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public Context context() {
        return context;
    }

    public void createDialogLevel(User user) {

        AlertDialog.Builder builder =  new AlertDialog.Builder(context());
        final View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.dialog_nivel, null);

        Button btnFacil = view.findViewById(R.id.btn_level_facil);
        Button btnNormal = view.findViewById(R.id.btn_level_normal);
        Button btnDificil = view.findViewById(R.id.btn_level_dificil);
        Button btnOtaku = view.findViewById(R.id.btn_level_dios);

        switch (iLevel){
            case 1: break;
            case 2: btnNormal.setAlpha(1f);
                    btnNormal.setEnabled(true);
                    break;
            case 3: btnNormal.setAlpha(1f);
                    btnNormal.setEnabled(true);
                    btnDificil.setAlpha(1f);
                    btnDificil.setEnabled(true);
                    break;
            case 4: btnNormal.setAlpha(1f);
                    btnNormal.setEnabled(true);
                    btnDificil.setAlpha(1f);
                    btnDificil.setEnabled(true);
                    btnOtaku.setAlpha(1f);
                    btnDificil.setEnabled(true);
                    break;
        }

        btnFacil.setOnClickListener(v -> {
            dialog.dismiss();
            startQuiz(idAnime,iLevel,user);
        });

        btnNormal.setOnClickListener(v -> {
            dialog.dismiss();
            startQuiz(idAnime,iLevel,user);
        });

        btnDificil.setOnClickListener(v -> {
            dialog.dismiss();
            startQuiz(idAnime,iLevel,user);
        });

        btnOtaku.setOnClickListener(v -> {
            dialog.dismiss();
            startQuiz(idAnime,iLevel,user);
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void startQuiz(int idAnime, int level, User user){
        Intent intent = new Intent(getContext(), PreguntasActivity.class);
        intent.putExtra("level",level);
        intent.putExtra("idAnime",idAnime);
        intent.putExtra("score",iScore);
        intent.putExtra("user",user);
        startActivity(intent);
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
            adapterFilter.setItemClickListener((Anime anime) -> menuPresenter.launchAnimeTest(anime));
            adapterFilter.setLstAnimes(lstAnimeFilter);
            recyclerViewAnimes.setAdapter(adapterFilter);
            adapterFilter.notifyDataSetChanged();

        }

        @Override
        public void afterTextChanged(Editable s) {
            //EMPTY
        }
    };
}
