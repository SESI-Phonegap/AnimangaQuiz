package com.sesi.chris.animangaquiz.view.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.client.QuizClient;
import com.sesi.chris.animangaquiz.data.model.UpdateResponse;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.interactor.FriendsInteractor;
import com.sesi.chris.animangaquiz.presenter.SearchFriendPresenter;
import com.sesi.chris.animangaquiz.view.adapter.FriendsAdapter;

import java.util.List;

public class SearchFriendActivity extends AppCompatActivity implements SearchFriendPresenter.View {

    private SearchFriendPresenter presenter;
    private EditText et_searchFriend;
    private RecyclerView rv_firends;
    private Context context;
    private ProgressBar progressBar;
    private List<User> lstUser;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        init();
    }

    public void init(){
        presenter = new SearchFriendPresenter(new FriendsInteractor(new QuizClient()));
        presenter.setView(this);
        context = getApplicationContext();
        et_searchFriend = findViewById(R.id.et_search);
        rv_firends = findViewById(R.id.rv_amigos);
        progressBar = findViewById(R.id.pb_friends);
        user = (User) getIntent().getSerializableExtra("user");
        setupRecyclerViewFriends();
        et_searchFriend.addTextChangedListener(textWatcherBuscar);

    }

    private void setupRecyclerViewFriends(){
        FriendsAdapter adapter = new FriendsAdapter();
        adapter.setItemClickListener((User userFriend) -> presenter.addFriend(user.getUserName(),user.getPassword(),user.getIdUser(),userFriend.getIdUser()));
        rv_firends.setLayoutManager(new LinearLayoutManager(this));
        rv_firends.setHasFixedSize(true);
        rv_firends.setAdapter(adapter);

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
        FriendsAdapter adapter = (FriendsAdapter) rv_firends.getAdapter();
        adapter.setLstUser(lstUser);
        adapter.notifyDataSetChanged();
        this.lstUser = lstUser;

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

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable queryUserName) {
            if (queryUserName.length() >= 3){
                presenter.searchFriend(user.getUserName(),user.getPassword(),queryUserName.toString());
            }
        }
    };
}

