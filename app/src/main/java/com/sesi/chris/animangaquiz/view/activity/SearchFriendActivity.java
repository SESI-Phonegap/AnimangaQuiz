package com.sesi.chris.animangaquiz.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.client.QuizClient;
import com.sesi.chris.animangaquiz.data.model.UpdateResponse;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.interactor.FriendsInteractor;
import com.sesi.chris.animangaquiz.presenter.SearchFriendPresenter;
import com.sesi.chris.animangaquiz.view.adapter.FriendsAdapter;
import java.util.List;

public class SearchFriendActivity extends AppCompatActivity implements SearchFriendPresenter.ViewSearchFriend {

    private SearchFriendPresenter presenter;
    private RecyclerView rvFirends;
    private Context context;
    private ProgressBar progressBar;
    private User user;
    private AdView mAdview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        init();
    }

    public void init(){
        presenter = new SearchFriendPresenter(new FriendsInteractor(new QuizClient()));
        presenter.setView(this);
        context = this;
        EditText etSearchFriend = findViewById(R.id.et_search);
        rvFirends = findViewById(R.id.rv_amigos);
        progressBar = findViewById(R.id.pb_friends);
        user = (User) getIntent().getSerializableExtra("user");
        setupRecyclerViewFriends();
        etSearchFriend.addTextChangedListener(textWatcherBuscar);
        cargarPublicidad();
    }

    private void cargarPublicidad(){
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
    }


    private void setupRecyclerViewFriends(){
        FriendsAdapter adapter = new FriendsAdapter();
        adapter.setItemClickListener((User userFriend) -> showConfirmDialog(userFriend.getIdUser()));
        rvFirends.setLayoutManager(new LinearLayoutManager(this));
        rvFirends.setHasFixedSize(true);
        rvFirends.setAdapter(adapter);

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
    public void showServerError(String error) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(context(),error,Toast.LENGTH_LONG).show();
    }

    @Override
    public void renderFriends(List<User> lstUser) {
        FriendsAdapter adapter = (FriendsAdapter) rvFirends.getAdapter();
        adapter.setLstUser(lstUser);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void renderAddFriend(UpdateResponse updateResponse) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this,updateResponse.error,Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public Context context() {
        return context;
    }

    TextWatcher textWatcherBuscar = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //Empty Method
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Empty Method
        }

        @Override
        public void afterTextChanged(Editable queryUserName) {
            if (queryUserName.length() >= 3){
                presenter.searchFriend(user.getUserName(),user.getPassword(),queryUserName.toString());
            }
        }
    };

    public void showConfirmDialog(int iIdFriend){
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
        dialog.show();

        tvMensaje.setText(context().getString(R.string.msg_confirmar_amigo));

        btnAceptar.setOnClickListener(v -> {
            presenter.addFriend(user.getUserName(),user.getPassword(),user.getIdUser(),iIdFriend);
            dialog.dismiss();
        });

        btnCancel.setOnClickListener((View v) -> dialog.dismiss());

    }
}

