package com.sesi.chris.animangaquiz.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.client.QuizClient;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.UpdateResponse;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.interactor.LoginInteractor;
import com.sesi.chris.animangaquiz.presenter.LoginPresenter;
import com.sesi.chris.animangaquiz.view.utils.UtilInternetConnection;
import com.sesi.chris.animangaquiz.view.utils.UtilsPreference;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements LoginPresenter.ViewLogin {

    private LoginPresenter loginPresenter;
    private ProgressBar progressBar;
    private EditText etUserName;
    private EditText etPassword;
    private Context context;
    private CheckBox cbGuardarUser;
    private CallbackManager callbackManager;
    private String sFacebookId;
    private String sFacebookName;
    private String sFacebookEmail;
    private TextView tvRegistro;
    private Button btnLogin;
    private static final String FACEBOOK_EMAIL = "email";
    private static final String FACEBOOK_NAME = "name";

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
        progressBar = findViewById(R.id.pb_login);
        etUserName = findViewById(R.id.et_userName);
        etPassword = findViewById(R.id.et_password);
        tvRegistro = findViewById(R.id.tv_registro);
        btnLogin = findViewById(R.id.btn_login);
        context = this;
        cbGuardarUser = findViewById(R.id.checkBox);
        facebookLogin();
        sharedPreferenceLogin();
    }

    private void facebookLogin(){
        //Facebook
        callbackManager = CallbackManager.Factory.create();
        LoginButton btnLoginFacebook = findViewById(R.id.btn_login_facebook);
        btnLoginFacebook.setReadPermissions(Arrays.asList("public_profile",FACEBOOK_EMAIL));
        btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                showLoading();
                blockUi();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {
                    hideLoading();
                    try {
                      //  URL photoFace = new URL("https://graph.facebook.com/" + object.getString("id") + "/picture?width=250&height=250");
                        sFacebookId = object.getString("id");
                        sFacebookName = object.getString(FACEBOOK_NAME);
                        sFacebookEmail = object.getString(FACEBOOK_EMAIL);

                        if (UtilInternetConnection.isOnline(context())) {
                            loginPresenter.validaUsuarioFacebook(sFacebookId);
                        } else {
                            Toast.makeText(context(),getString(R.string.noInternet),Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        Log.e("Error-",e.getMessage());
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields","id,email,name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                desBlockUi();
            }

            @Override
            public void onError(FacebookException error) {
                desBlockUi();
                Log.d("ERROR--",error.toString());

            }
        });
        if (AccessToken.getCurrentAccessToken() != null){
            blockUi();
            String sFaceIdEmail = AccessToken.getCurrentAccessToken().getUserId();
            if (UtilInternetConnection.isOnline(context())) {
                loginPresenter.validaUsuarioFacebook(sFaceIdEmail);
            } else {
                Toast.makeText(context(),getString(R.string.noInternet),Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sharedPreferenceLogin(){
        List<String> lstDataUser = UtilsPreference.getUserDataLogin(context());
        if (!lstDataUser.get(0).isEmpty() && !lstDataUser.get(1).isEmpty()){
            blockUi();
            tvRegistro.setVisibility(View.GONE);
            if (UtilInternetConnection.isOnline(context())){
                loginPresenter.onLogin(lstDataUser.get(0),lstDataUser.get(1));
            } else {
                Toast.makeText(context(),getString(R.string.noInternet),Toast.LENGTH_LONG).show();
            }
        } else {
            //Stilo de texto tipo Link para Registro
            SpannableString content = new SpannableString(getString(R.string.registrarse));
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            tvRegistro.setText(content);
            btnLogin.setOnClickListener(v -> {
                if (!etUserName.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()){
                    if (UtilInternetConnection.isOnline(context())) {
                        blockUi();
                        loginPresenter.onLogin(etUserName.getText().toString(), etPassword.getText().toString());
                    } else {
                        Toast.makeText(context(),getString(R.string.noInternet),Toast.LENGTH_LONG).show();
                    }
                }
            });
            tvRegistro.setOnClickListener(v -> {
                Intent intent = new Intent(this,RegistroActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }

    public void blockUi(){
        etUserName.setEnabled(false);
        etPassword.setEnabled(false);
        btnLogin.setEnabled(false);
        btnLogin.setClickable(false);
        tvRegistro.setEnabled(false);
    }

    public void desBlockUi(){
        etUserName.setEnabled(true);
        etPassword.setEnabled(true);
        btnLogin.setEnabled(true);
        btnLogin.setClickable(true);
        tvRegistro.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
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
    public void showServerError(String error) {
        Toast.makeText(context(),getString(R.string.serverError,error),Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void renderLogin(LoginResponse loginResponse) {
        User user = loginResponse.getUser();
        if (null != user) {
            if (cbGuardarUser.isChecked()) {
                UtilsPreference.savePreferenceUserLogin(context(), user.getUserName(), user.getPassword());
            }
            Intent intent = new Intent(context(),MenuActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
            finish();
        } else {
            desBlockUi();
            Toast.makeText(context(), loginResponse.getError(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void updateGemsResponse(UpdateResponse updateResponse) {
        Toast.makeText(context(),R.string.msgGemas + updateResponse.estatus,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showUpdateGemsError() {
        Toast.makeText(context(),R.string.updateGemsError,Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateAvatarResponse(UpdateResponse updateResponse) {
        //Empty Method
    }

    @Override
    public void showUpdateAvatarError() {
        //Empty Method
    }

    @Override
    public void renderResponse(UpdateResponse updateResponse) {
        Toast.makeText(context(),updateResponse.error,Toast.LENGTH_LONG).show();
    }

    @Override
    public void renderResponseFacebook(UpdateResponse updateResponse) {
        if (null != updateResponse){
            if (updateResponse.estatus.equals("200")){
                Toast.makeText(context(),getString(R.string.msg_registro_exitoso),Toast.LENGTH_LONG).show();
                loginPresenter.validaUsuarioFacebook(sFacebookId);
            } else {
                desBlockUi();
                Toast.makeText(context(),updateResponse.error,Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void renderLoginFacbook(LoginResponse loginResponse) {
        User user = loginResponse.getUser();
        if (user != null){
            Intent intent = new Intent(context(),MenuActivity.class);
            intent.putExtra("user",user);
            intent.putExtra(FACEBOOK_NAME,sFacebookName);
            intent.putExtra(FACEBOOK_EMAIL,sFacebookEmail);
            startActivity(intent);
            finish();
        } else {
            loginPresenter.onRegisterNewUserFacebook("",sFacebookId,sFacebookName,sFacebookEmail,0,"","");
        }
    }

}
