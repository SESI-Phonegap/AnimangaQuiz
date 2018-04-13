package com.sesi.chris.animangaquiz.view.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.model.Preguntas;
import com.sesi.chris.animangaquiz.presenter.PreguntasPresenter;

import java.util.List;

public class PreguntasActivity extends AppCompatActivity implements PreguntasPresenter.View{

    private Context context;
    private PreguntasPresenter presenter;
    private TextView tv_pregunta;
    private RecyclerView rv_respuestas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);
        init();
    }

    public void init(){
        context = this;
    }

    @Override
    protected void onDestroy() {
        presenter.terminate();
        super.onDestroy();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showQuestionsNotFoundMessage() {

    }

    @Override
    public void showConnectionErrorMessage() {

    }

    @Override
    public void showServerError() {

    }

    @Override
    public void renderQuestions(List<Preguntas> lstPreguntas) {

    }

    @Override
    public void calcularPuntos() {

    }

    @Override
    public Context context() {
        return context;
    }
}
