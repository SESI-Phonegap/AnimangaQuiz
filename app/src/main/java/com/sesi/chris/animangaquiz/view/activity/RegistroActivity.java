package com.sesi.chris.animangaquiz.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.sesi.chris.animangaquiz.R;

public class RegistroActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etNombre;
    private EditText etEmail;
    private EditText etEdad;
    private EditText etPassword;
    private RadioGroup radioGroup;
    private Button btnRegistrar;
    private ProgressBar pbRegistro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        init();
    }

    private void init(){
        etUsername = findViewById(R.id.et_userName);
        etNombre = findViewById(R.id.et_nombre);
        etEmail = findViewById(R.id.et_email);
        etEdad = findViewById(R.id.et_edad);
        etPassword = findViewById(R.id.et_password);
        radioGroup = findViewById(R.id.radioGroup);
        btnRegistrar = findViewById(R.id.btn_registrar);
        pbRegistro = findViewById(R.id.pb_registro);
    }
}
