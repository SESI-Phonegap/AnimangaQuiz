package com.sesi.chris.animangaquiz.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.client.QuizClient;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.UpdateResponse;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.interactor.RegistroNuevoUsuarioInteractor;
import com.sesi.chris.animangaquiz.presenter.RegistroNuevoUsuarioPresenter;
import com.sesi.chris.animangaquiz.view.utils.UtilInternetConnection;

public class RegistroActivity extends AppCompatActivity implements RegistroNuevoUsuarioPresenter.View{

    private EditText etUsername;
    private EditText etNombre;
    private EditText etEmail;
    private EditText etEdad;
    private EditText etPassword;
    private ProgressBar pbRegistro;
    private Context context;
    private RegistroNuevoUsuarioPresenter presenter;
    private String sGenero;
    private String sUserName;
    private String sPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        init();
    }

    private void init(){
        context = this;
        presenter = new RegistroNuevoUsuarioPresenter(new RegistroNuevoUsuarioInteractor(new QuizClient()));
        presenter.setView(this);
        etUsername = findViewById(R.id.et_userName);
        etNombre = findViewById(R.id.et_nombre);
        etEmail = findViewById(R.id.et_email);
        etEdad = findViewById(R.id.et_edad);
        etPassword = findViewById(R.id.et_password);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        Button btnRegistrar = findViewById(R.id.btn_registrar);
        pbRegistro = findViewById(R.id.pb_registro);

        radioGroup.setOnCheckedChangeListener((radioGroup1, id) -> {
            switch (id){
                case R.id.radioHombre:
                    sGenero = "H";
                    break;

                case R.id.radioMujer:
                    sGenero = "M";
                    break;
            }
        });

        btnRegistrar.setOnClickListener(v -> {
            if (UtilInternetConnection.isOnline(this)){
                sUserName = etUsername.getText().toString();
                String sNombre = etNombre.getText().toString();
                String sEmail = etEmail.getText().toString();
                String sEdad = etEdad.getText().toString();
                sPassword = etPassword.getText().toString();

                if (!sUserName.equals("") && !sNombre.equals("") && !sEmail.equals("") && !sEdad.equals("") && !sPassword.equals("") && sGenero != null){
                    presenter.registroNuevoUsuario(sUserName,sNombre,sEmail,Integer.parseInt(sEdad),sGenero,sPassword);
                } else {
                    Toast.makeText(context(),getString(R.string.datosincompletos),Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(context(),getString(R.string.noInternet),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void showLoading() {
        pbRegistro.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pbRegistro.setVisibility(View.GONE);
    }

    @Override
    public void showLoginNotFoundMessage() {

    }

    @Override
    public void showServerError(String error) {
        pbRegistro.setVisibility(View.GONE);
    }

    @Override
    public void renderResponse(UpdateResponse updateResponse) {
        if (null != updateResponse){
            if (updateResponse.estatus.equals("200")){
                Toast.makeText(context(),getString(R.string.msg_registro_exitoso),Toast.LENGTH_LONG).show();
                presenter.onLogin(sUserName,sPassword);
            } else {
                Toast.makeText(context(),updateResponse.error,Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void renderLogin(LoginResponse loginResponse) {
        User user = loginResponse.getUser();
        // Log.d("Respuesta--",loginResponse.getEstatus());
        if (null != user) {
            Intent intent = new Intent(context(),MenuActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
            finish();
        } else {
            // Log.d("", loginResponse.getError());
            Toast.makeText(context(),loginResponse.getError(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public Context context() {
        return context;
    }
}
