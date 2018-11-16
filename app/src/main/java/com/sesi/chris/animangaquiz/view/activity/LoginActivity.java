package com.sesi.chris.animangaquiz.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.client.QuizClient;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.UpdateResponse;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.data.model.Wallpaper;
import com.sesi.chris.animangaquiz.interactor.LoginInteractor;
import com.sesi.chris.animangaquiz.presenter.LoginPresenter;
import com.sesi.chris.animangaquiz.view.utils.UtilInternetConnection;
import com.sesi.chris.animangaquiz.view.utils.UtilsPreference;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class LoginActivity extends AppCompatActivity implements LoginPresenter.View {

    private LoginPresenter loginPresenter;
    private ProgressBar progressBar;
    private EditText et_username;
    private EditText et_password;
    private Context context;
    private CheckBox cbGuardarUser;
    private CallbackManager callbackManager;
    private String sFaceIdEmail;
    private boolean bFacebookLogin = false;
    private String sFacebookId;
    private String sFacebookName;
    private String sFacebookEmail;
    private TextView tv_registro;
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
        ConstraintLayout background = findViewById(R.id.Constraint_background);
        ((AnimationDrawable) background.getBackground()).start();
        progressBar = findViewById(R.id.pb_login);
        et_username = findViewById(R.id.et_userName);
        et_password = findViewById(R.id.et_password);
        tv_registro = findViewById(R.id.tv_registro);
        btn_login = findViewById(R.id.btn_login);
        context = this;
        cbGuardarUser = findViewById(R.id.checkBox);

        //Facebook
        callbackManager = CallbackManager.Factory.create();
        LoginButton btnLoginFacebook = findViewById(R.id.btn_login_facebook);
        btnLoginFacebook.setReadPermissions(Arrays.asList("public_profile","email"));
        btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                showLoading();
                blockUi();
                String sAccessToken = loginResult.getAccessToken().getToken();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        hideLoading();
                        Log.i("JSON-DATA--",object.toString());
                        Log.i("RESPONSE---",response.toString());
                        bFacebookLogin = true;
                        try {
                            URL photoFace = new URL("https://graph.facebook.com/" + object.getString("id") + "/picture?width=250&height=250");
                            sFacebookId = object.getString("id");
                            sFacebookName = object.getString("name");
                            sFacebookEmail = object.getString("email");

                            if (UtilInternetConnection.isOnline(context())) {
                                loginPresenter.validaUsuarioFacebook(sFacebookId);
                            } else {
                                Toast.makeText(context(),getString(R.string.noInternet),Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
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
            sFaceIdEmail = AccessToken.getCurrentAccessToken().getUserId();
            if (UtilInternetConnection.isOnline(context())) {
                loginPresenter.validaUsuarioFacebook(sFaceIdEmail);
            } else {
                Toast.makeText(context(),getString(R.string.noInternet),Toast.LENGTH_LONG).show();
            }
        }


        List<String> lstDataUser = UtilsPreference.getUserDataLogin(context());
        if (!lstDataUser.get(0).isEmpty() && !lstDataUser.get(1).isEmpty()){
            et_password.setFocusable(false);
            et_username.setFocusable(false);
            cbGuardarUser.setEnabled(false);
            tv_registro.setVisibility(View.GONE);
            btn_login.setEnabled(false);

            if (UtilInternetConnection.isOnline(context())){
                loginPresenter.onLogin(lstDataUser.get(0),lstDataUser.get(1));
            } else {
                Toast.makeText(context(),getString(R.string.noInternet),Toast.LENGTH_LONG).show();
            }
        } else {

            //Stilo de texto tipo Link para Registro
            SpannableString content = new SpannableString(getString(R.string.registrarse));
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            tv_registro.setText(content);

            btn_login.setOnClickListener(v -> {
                if (!et_username.getText().toString().isEmpty() && !et_password.getText().toString().isEmpty()){
                    if (UtilInternetConnection.isOnline(context())) {
                        blockUi();
                        loginPresenter.onLogin(et_username.getText().toString(), et_password.getText().toString());
                    } else {
                        Toast.makeText(context(),getString(R.string.noInternet),Toast.LENGTH_LONG).show();
                    }
                }
            });

            tv_registro.setOnClickListener(v -> {
                Intent intent = new Intent(this,RegistroActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }

    public void blockUi(){
        et_username.setEnabled(false);
        et_password.setEnabled(false);
        btn_login.setEnabled(false);
        btn_login.setClickable(false);
        tv_registro.setEnabled(false);
    }

    public void desBlockUi(){
        et_username.setEnabled(true);
        et_password.setEnabled(true);
        btn_login.setEnabled(true);
        btn_login.setClickable(true);
        tv_registro.setEnabled(true);
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
       // Log.d("Respuesta--",loginResponse.getEstatus());
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

    }

    @Override
    public void showUpdateAvatarError() {

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
            intent.putExtra("name",sFacebookName);
            intent.putExtra("email",sFacebookEmail);
            startActivity(intent);
            finish();
        } else {
            loginPresenter.onRegisterNewUserFacebook("",sFacebookId,sFacebookName,sFacebookEmail,0,"","");
            //Toast.makeText(context(), loginResponse.getError(), Toast.LENGTH_LONG).show();
        }
    }

}
