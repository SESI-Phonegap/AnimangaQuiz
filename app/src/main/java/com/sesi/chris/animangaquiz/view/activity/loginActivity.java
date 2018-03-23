package com.sesi.chris.animangaquiz.view.activity;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.client.QuizClient;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.interactor.LoginInteractor;
import com.sesi.chris.animangaquiz.presenter.LoginPresenter;

import java.util.List;

public class loginActivity extends AppCompatActivity implements LoginPresenter.View {

    private LoginPresenter loginPresenter;
    private ConstraintLayout background;
    private ProgressBar progressBar;
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    @Override
    protected void onDestroy() {
        loginPresenter.terminate();
        super.onDestroy();
    }

    public void init (){
        loginPresenter = new LoginPresenter(new LoginInteractor(new QuizClient()));
        loginPresenter.setView(this);
        background = findViewById(R.id.Constraint_background);
        progressBar = findViewById(R.id.pb_login);
        et_username = findViewById(R.id.et_userName);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(v -> {
          if (!et_username.getText().toString().isEmpty() && !et_password.getText().toString().isEmpty()){
              loginPresenter.onLogin(et_username.getText().toString(),et_password.getText().toString());
          }
        });
    }

    @Override
    public Context context() {
        return null;
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
    public void showLoginNotFoundMessage() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showConnectionErrorMessage() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showServerError() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void renderLogin(LoginResponse loginResponse) {

        User user = loginResponse.getUser();
        Log.d("Respuesta--",loginResponse.getEstatus());
        if (null != user) {
            Log.d("LOGINPRESENTER--", user.getName());
        } else {
            Log.d("", loginResponse.getError());
        }
    }
}
