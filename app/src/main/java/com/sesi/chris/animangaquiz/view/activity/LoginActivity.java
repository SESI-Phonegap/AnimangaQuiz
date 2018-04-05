package com.sesi.chris.animangaquiz.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.client.QuizClient;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.interactor.LoginInteractor;
import com.sesi.chris.animangaquiz.presenter.LoginPresenter;
import com.sesi.chris.animangaquiz.view.utils.UtilInternetConnection;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements LoginPresenter.View {

    private LoginPresenter loginPresenter;
    private ConstraintLayout background;
    private ProgressBar progressBar;
    private EditText et_username;
    private EditText et_password;
    private TextView tv_registro;
    private Button btn_login;
    private Context context;
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
        tv_registro = findViewById(R.id.tv_registro);
        btn_login = findViewById(R.id.btn_login);
        context = this;

        //Stilo de texto tipo Link para Registro
        SpannableString content = new SpannableString(getString(R.string.registrarse));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tv_registro.setText(content);

        btn_login.setOnClickListener(v -> {
          if (!et_username.getText().toString().isEmpty() && !et_password.getText().toString().isEmpty()){
              if (UtilInternetConnection.isOnline(context())) {
                  loginPresenter.onLogin(et_username.getText().toString(), et_password.getText().toString());
              } else {
                  Toast.makeText(context(),getString(R.string.noInternet),Toast.LENGTH_LONG).show();
              }
          }
        });

        tv_registro.setOnClickListener(v -> {

        });
    }

    @Override
    public Context context() {
        return context;
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
       // Log.d("Respuesta--",loginResponse.getEstatus());
        if (null != user) {
            Intent intent = new Intent(context(),MenuActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user",user);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
           // Log.d("", loginResponse.getError());
            Toast.makeText(context(),loginResponse.getError(),Toast.LENGTH_LONG).show();
        }
    }
}
