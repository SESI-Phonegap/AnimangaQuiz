package com.sesi.chris.animangaquiz.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.client.QuizClient;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.UpdateResponse;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.interactor.LoginInteractor;
import com.sesi.chris.animangaquiz.presenter.LoginPresenter;
import com.sesi.chris.animangaquiz.view.utils.UtilInternetConnection;
import com.sesi.chris.animangaquiz.view.utils.UtilsPreference;

import java.util.List;

public class SplashActivity extends AppCompatActivity implements LoginPresenter.ViewLogin{

    private LoginPresenter loginPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loginPresenter = new LoginPresenter(new LoginInteractor(new QuizClient()));
        loginPresenter.setView(this);

        Handler handler = new Handler();
        Runnable runnable = this::sharedPreferenceLogin;
        handler.postDelayed(runnable,1200);

    }

    public void openMainActivity(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void sharedPreferenceLogin() {
        List<String> lstDataUser = UtilsPreference.getUserDataLogin(this);
        if (!lstDataUser.get(0).isEmpty() && !lstDataUser.get(1).isEmpty()) {
            if (UtilInternetConnection.isOnline(context())) {
                loginPresenter.onLogin(lstDataUser.get(0), lstDataUser.get(1));
            } else {
                Toast.makeText(context(), getString(R.string.noInternet), Toast.LENGTH_LONG).show();
            }
        } else {
            openMainActivity();
        }
    }

    @Override
    protected void onDestroy() {
        loginPresenter.terminate();
        super.onDestroy();
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
    public void showServerError(String error) {

    }

    @Override
    public void renderLogin(LoginResponse loginResponse) {
        User user = loginResponse.getUser();
        if (null != user) {
            Intent intent = new Intent(context(),MenuActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(context(), loginResponse.getError(), Toast.LENGTH_LONG).show();
            openMainActivity();
        }
    }

    @Override
    public void updateGemsResponse(UpdateResponse updateResponse) {

    }

    @Override
    public void showUpdateGemsError() {

    }

    @Override
    public void updateAvatarResponse(UpdateResponse updateResponse) {

    }

    @Override
    public void showUpdateAvatarError() {

    }

    @Override
    public void renderResponse(UpdateResponse updateResponse) {

    }

    @Override
    public void renderResponseFacebook(UpdateResponse updateResponse) {

    }

    @Override
    public void renderLoginFacbook(LoginResponse loginResponse) {

    }

    @Override
    public void deleteUserAction() {

    }

    @Override
    public Context context() {
        return this;
    }
}
