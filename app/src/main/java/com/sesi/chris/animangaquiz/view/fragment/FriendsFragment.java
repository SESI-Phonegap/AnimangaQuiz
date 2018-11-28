package com.sesi.chris.animangaquiz.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.client.QuizClient;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.interactor.FriendsByUserInteractor;
import com.sesi.chris.animangaquiz.presenter.FriendsByUserPresenter;
import com.sesi.chris.animangaquiz.view.activity.LoginActivity;
import com.sesi.chris.animangaquiz.view.activity.SearchFriendActivity;
import com.sesi.chris.animangaquiz.view.adapter.FriendsByUserAdapter;
import com.sesi.chris.animangaquiz.view.utils.UtilInternetConnection;
import java.util.List;
import java.util.Objects;


public class FriendsFragment extends Fragment implements FriendsByUserPresenter.ViewFriendsByUser {

    private FriendsByUserPresenter presenter;
    private RecyclerView rvFriends;
    private ProgressBar pbFriends;
    private Context context;


    public FriendsFragment() {
        // Required empty public constructor
    }


    public static FriendsFragment newInstance() {
        return new FriendsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    public void init(){
        context = getContext();
        presenter = new FriendsByUserPresenter(new FriendsByUserInteractor(new QuizClient()));
        presenter.setView(this);
        FloatingActionButton btnAddFriend = getActivity().findViewById(R.id.floatButton);
        pbFriends = Objects.requireNonNull(getActivity()).findViewById(R.id.pb_friend_fragment);
        rvFriends = Objects.requireNonNull(getActivity()).findViewById(R.id.rv_friends);
        User user = (User) getActivity().getIntent().getSerializableExtra("user");
        setupRecyclerView();
        if (UtilInternetConnection.isOnline(context())){
            if (null != user){
                presenter.getAllFriendsByUser(user.getUserName(),user.getPassword());
            } else {
                Toast.makeText(context(),"Ocurrio un error",Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context(),getString(R.string.noInternet),Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context(),LoginActivity.class);
            startActivity(intent);
        }

        btnAddFriend.setOnClickListener(v -> {
            Intent intent = new Intent(context(),SearchFriendActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
        });
    }

    public void setupRecyclerView(){
        FriendsByUserAdapter adapter = new FriendsByUserAdapter();
        adapter.setItemClickListener(user -> presenter.clickFriend(user));
        rvFriends.setHasFixedSize(true);
        rvFriends.setLayoutManager(new LinearLayoutManager(context()));
        rvFriends.setAdapter(adapter);
    }

    @Override
    public void showLoading() {
        pbFriends.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pbFriends.setVisibility(View.GONE);
    }

    @Override
    public void showFriendsNotFoundMessage() {
        pbFriends.setVisibility(View.GONE);
    }

    @Override
    public void showServerError(String error) {
        pbFriends.setVisibility(View.GONE);
        Toast.makeText(context(),error,Toast.LENGTH_LONG).show();
    }

    @Override
    public void renderFriends(List<User> lstUser) {
        FriendsByUserAdapter adapter = (FriendsByUserAdapter) rvFriends.getAdapter();
        adapter.setLstUser(lstUser);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void clickFirend(User user) {
        Toast.makeText(context(),user.getUserName(),Toast.LENGTH_LONG).show();
    }

    @Override
    public Context context() {
        return context;
    }
}
