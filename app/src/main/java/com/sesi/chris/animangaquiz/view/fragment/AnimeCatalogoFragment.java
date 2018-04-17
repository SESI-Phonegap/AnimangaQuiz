package com.sesi.chris.animangaquiz.view.fragment;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.client.QuizClient;
import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.interactor.MenuInteractor;
import com.sesi.chris.animangaquiz.presenter.MenuPresenter;
import com.sesi.chris.animangaquiz.view.activity.LoginActivity;
import com.sesi.chris.animangaquiz.view.activity.PreguntasActivity;
import com.sesi.chris.animangaquiz.view.adapter.AnimeAdapter;
import com.sesi.chris.animangaquiz.view.utils.UtilInternetConnection;

import java.util.ArrayList;
import java.util.List;

public class AnimeCatalogoFragment extends Fragment implements MenuPresenter.View, SearchView.OnQueryTextListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "usuario";
    private static final int FACIL = 1;
    private static final int MEDIO = 2;
    private static final int DIFICIL = 3;
    private static final int OTAKU = 4;
    private MenuPresenter menuPresenter;
    private RecyclerView recyclerViewAnimes;
    private Toolbar toolbar;
    private Context context;
    private AlertDialog dialog;
    private User user;
    private TextView et_Search;
    private ConstraintLayout constraintLayoutSearch;
    private List<Anime> lstAnime;

    // TODO: Rename and change types of parameters
    private String mParam1;

    public AnimeCatalogoFragment() {
        // Required empty public constructor
    }

    public static AnimeCatalogoFragment newInstance() {
        AnimeCatalogoFragment fragment = new AnimeCatalogoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

        et_Search = getActivity().findViewById(R.id.et_search);
        et_Search.addTextChangedListener(textWatcherFilter);
        constraintLayoutSearch = getActivity().findViewById(R.id.constraintSearch);
        recyclerViewAnimes = getActivity().findViewById(R.id.recyclerViewAnime);
        Bundle bundle =  getActivity().getIntent().getExtras();
        user = (User) bundle.getSerializable("user");
        if (UtilInternetConnection.isOnline(context())){
            if (null != user) {
                menuPresenter.getAllAnimes(user.getUserName(), user.getPassword());
                setupRecyclerView();
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

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showAnimesNotFoundMessage() {

    }

    @Override
    public void showConnectionErrorMessage() {

    }

    @Override
    public void showServerError() {

    }

    @Override
    public void renderAnimes(List<Anime> lstAnimes) {
        AnimeAdapter adapter = (AnimeAdapter) recyclerViewAnimes.getAdapter();
        adapter.setLstAnimes(lstAnimes);
        adapter.notifyDataSetChanged();
        this.lstAnime = lstAnimes;
    }

    @Override
    public void launchAnimeTest(Anime anime) {
        createDialogLevel(anime.getIdAnime(),user.getUserName(),user.getPassword());
    }

    @Override
    public Context context() {
        return context;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void createDialogLevel(String idAnime, String usuario, String passw) {

        AlertDialog.Builder builder =  new AlertDialog.Builder(context());
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_nivel, null);

        Button btnFacil = view.findViewById(R.id.btn_level_facil);
        Button btnNormal = view.findViewById(R.id.btn_level_normal);
        Button btnDificil = view.findViewById(R.id.btn_level_dificil);
        Button btnOtaku = view.findViewById(R.id.btn_level_dios);

        btnFacil.setOnClickListener(v -> {
            dialog.dismiss();
            startQuiz(idAnime,FACIL,usuario,passw);
        });

        btnNormal.setOnClickListener(v -> {
            dialog.dismiss();
            startQuiz(idAnime,MEDIO,usuario,passw);
        });

        btnDificil.setOnClickListener(v -> {
            dialog.dismiss();
            startQuiz(idAnime,DIFICIL,usuario,passw);
        });

        btnOtaku.setOnClickListener(v -> {
            dialog.dismiss();
            startQuiz(idAnime,OTAKU,usuario,passw);
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void startQuiz(String idAnime, int level, String userName, String pass){
        Intent intent = new Intent(getContext(), PreguntasActivity.class);
        intent.putExtra("level",level);
        intent.putExtra("anime",idAnime);
        intent.putExtra("userName",userName);
        intent.putExtra("pass",pass);
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
