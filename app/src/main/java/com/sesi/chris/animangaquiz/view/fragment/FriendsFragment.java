package com.sesi.chris.animangaquiz.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.client.QuizClient;
import com.sesi.chris.animangaquiz.data.dto.InternetDto;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.interactor.FriendsByUserInteractor;
import com.sesi.chris.animangaquiz.presenter.FriendsByUserPresenter;
import com.sesi.chris.animangaquiz.view.activity.LoginActivity;
import com.sesi.chris.animangaquiz.view.activity.SearchFriendActivity;
import com.sesi.chris.animangaquiz.view.adapter.FriendsByUserAdapter;
import com.sesi.chris.animangaquiz.view.utils.InternetUtil;

import java.util.List;


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
        View viewRoot = inflater.inflate(R.layout.fragment_friends, container, false);
        init(viewRoot);
        return viewRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void init(View viewRoot){
        context = getContext();
        presenter = new FriendsByUserPresenter(new FriendsByUserInteractor(new QuizClient()));
        presenter.setView(this);
        FloatingActionButton btnAddFriend = viewRoot.findViewById(R.id.floatButton);
        pbFriends = viewRoot.findViewById(R.id.pb_friend_fragment);
        rvFriends = viewRoot.findViewById(R.id.rv_friends);
        User user = (User) requireActivity().getIntent().getSerializableExtra("user");
        setupRecyclerView();
        InternetDto internetDto = InternetUtil.INSTANCE.getConnection(context());
        if (internetDto.isOnline()){
            if (null != user){
                presenter.getAllFriendsByUser(user.getEmail(),user.getPassword());
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
