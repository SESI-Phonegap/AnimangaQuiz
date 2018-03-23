package com.sesi.chris.animangaquiz.view.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.client.QuizClient;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.interactor.LoginInteractor;
import com.sesi.chris.animangaquiz.presenter.LoginPresenter;

import java.util.List;

public class loginActivity extends AppCompatActivity implements LoginPresenter.View {

    private LoginPresenter loginPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginPresenter = new LoginPresenter(new LoginInteractor(new QuizClient()));
        loginPresenter.setView(this);
        loginPresenter.onLogin("chris_slash10","Mexico-17");

    }

    @Override
    public Context context() {
        return null;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showLoginNotFoundMessage() {

    }

    @Override
    public void showConnectionErrorMessage() {

    }

    @Override
    public void showServerError() {

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
