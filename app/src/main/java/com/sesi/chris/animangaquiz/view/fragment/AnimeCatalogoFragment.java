package com.sesi.chris.animangaquiz.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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
import com.sesi.chris.animangaquiz.view.activity.PreguntasImgActivity;
import com.sesi.chris.animangaquiz.view.adapter.AnimeAdapter;
import com.sesi.chris.animangaquiz.view.utils.UtilInternetConnection;
import com.sesi.chris.animangaquiz.view.utils.Utils;

import java.util.List;

public class AnimeCatalogoFragment extends Fragment implements MenuPresenter.ViewMenu {

    private MenuPresenter menuPresenter;
    private RecyclerView recyclerViewAnimes;
    private ProgressBar progressBar;
    private Context context;
    private AlertDialog dialog;
    private User user;
    private List<Anime> lstAnime;
    private int iLevel;
    private int iScore;
    private int idAnime;
    private String sOpcion;
    private static final String ARG_OPCION = "opcion";
    private static final String QUIZ_IMG = "img";

    public AnimeCatalogoFragment() {
        // Required empty public constructor
    }

    public static AnimeCatalogoFragment newInstance(String sOp) {
        AnimeCatalogoFragment fragment = new AnimeCatalogoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_OPCION, sOp);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sOpcion = getArguments().getString(ARG_OPCION);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_anime_catalogo, container, false);
        init(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void init(View rootView) {
        Log.i("AnimeFragment", "Iniciado");
        context = getContext();
        menuPresenter = new MenuPresenter(new MenuInteractor(new QuizClient()));
        menuPresenter.setView(this);

        TextView etSearch = rootView.findViewById(R.id.et_search);
        etSearch.addTextChangedListener(textWatcherFilter);
        recyclerViewAnimes = rootView.findViewById(R.id.recyclerViewAnime);
        progressBar = rootView.findViewById(R.id.pb_login);
        user = (User) requireActivity().getIntent().getSerializableExtra("user");
        setupRecyclerView();
        if (UtilInternetConnection.isOnline(context())) {
            if (null != user) {
                if (sOpcion.equals(QUIZ_IMG)) {
                    menuPresenter.getAllAnimesImg(user.getEmail(), user.getPassword());
                } else {
                    menuPresenter.getAllAnimes(user.getEmail(), user.getPassword());
                }
            } else {
                Toast.makeText(context(), "Ocurrio un error", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context(), getString(R.string.noInternet), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context(), LoginActivity.class);
            startActivity(intent);
        }
    }

    private void setupRecyclerView() {
        AnimeAdapter adapter = new AnimeAdapter();
        adapter.setItemClickListener((Anime anime) -> menuPresenter.launchAnimeTest(anime));
        recyclerViewAnimes.setAdapter(adapter);
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
        progressBar.setVisibility(View.GONE);
        Toast.makeText(context(), error, Toast.LENGTH_LONG).show();
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
            iScore = Integer.parseInt(score.getPuntos());
            if (sOpcion.equals(QUIZ_IMG)){
                startQuizImg(idAnime,user);
            } else {
                startQuiz(idAnime, 1, user);
                //createDialogLevel(user);
            }
        } else {
            Toast.makeText(context(), scoreResponse.getError(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showScoreError() {
        Toast.makeText(context(), "Error !!!!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void launchAnimeTest(Anime anime) {
        if (UtilInternetConnection.isOnline(context())) {
            idAnime = anime.getIdAnime();
            menuPresenter.checkScoreAndLevel(user.getEmail(), user.getPassword(), anime.getIdAnime(), user.getIdUser());

        } else {
            Toast.makeText(context(), getString(R.string.noInternet), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public Context context() {
        return context;
    }

    public void createDialogLevel(User user) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context());
        final View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_nivel, null);

        Button btnFacil = view.findViewById(R.id.btn_level_facil);
        Button btnNormal = view.findViewById(R.id.btn_level_normal);
        Button btnDificil = view.findViewById(R.id.btn_level_dificil);
        Button btnOtaku = view.findViewById(R.id.btn_level_dios);

        switch (iLevel) {
            case 1:
                break;
            case 2:
                btnNormal.setAlpha(1f);
                btnNormal.setEnabled(true);
                break;
            case 3:
                btnNormal.setAlpha(1f);
                btnNormal.setEnabled(true);
                btnDificil.setAlpha(1f);
                btnDificil.setEnabled(true);
                break;
            case 4:
                btnNormal.setAlpha(1f);
                btnNormal.setEnabled(true);
                btnDificil.setAlpha(1f);
                btnDificil.setEnabled(true);
                btnOtaku.setAlpha(1f);
                btnOtaku.setEnabled(true);
                break;
            default:
                //Empty
                break;
        }

        btnFacil.setOnClickListener(v -> {
            dialog.dismiss();
            startQuiz(idAnime, 1, user);
        });

        btnNormal.setOnClickListener(v -> {
            dialog.dismiss();
            startQuiz(idAnime, 2, user);
        });

        btnDificil.setOnClickListener(v -> {
            dialog.dismiss();
            startQuiz(idAnime, 3, user);
        });

        btnOtaku.setOnClickListener(v -> {
            dialog.dismiss();
            startQuiz(idAnime, 4, user);
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void startQuiz(int idAnime, int level, User user) {
        Intent intent = new Intent(getContext(), PreguntasActivity.class);
        intent.putExtra("level", level);
        intent.putExtra("idAnime", idAnime);
        intent.putExtra("score", iScore);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    public void startQuizImg(int idAnime, User user) {
        Intent intent = new Intent(getContext(), PreguntasImgActivity.class);
        intent.putExtra("idAnime", idAnime);
        intent.putExtra("score", iScore);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    TextWatcher textWatcherFilter = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //EMPTY
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
          /*  List<Anime> lstAnimeFilter = new ArrayList<>();
            for (Anime anime : lstAnime){
               String textAnime = anime.getName().toLowerCase();
               if (textAnime.contains(s)){
                   lstAnimeFilter.add(anime);
               }
            }*/
            AnimeAdapter adapterFilter = new AnimeAdapter();
            adapterFilter.setItemClickListener((Anime anime) -> menuPresenter.launchAnimeTest(anime));
            adapterFilter.setLstAnimes(Utils.filtrarAnime(lstAnime, s));
            recyclerViewAnimes.setAdapter(adapterFilter);
            adapterFilter.notifyDataSetChanged();

        }

        @Override
        public void afterTextChanged(Editable s) {
            //EMPTY
        }
    };

}
