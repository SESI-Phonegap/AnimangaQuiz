package com.sesi.chris.animangaquiz.view.activity;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.client.QuizClient;
import com.sesi.chris.animangaquiz.data.model.Preguntas;
import com.sesi.chris.animangaquiz.data.model.Respuesta;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.interactor.PreguntasInteractor;
import com.sesi.chris.animangaquiz.presenter.PreguntasPresenter;
import com.sesi.chris.animangaquiz.view.adapter.RespuestasAdapter;
import com.sesi.chris.animangaquiz.view.utils.UtilInternetConnection;

import java.util.List;

public class PreguntasActivity extends AppCompatActivity implements PreguntasPresenter.View{

    private static final String TRUE = "1";
    private static final int TIME_QUESTIONS = 15000;
    private Context context;
    private PreguntasPresenter presenter;
    private TextView tv_pregunta;
    private RecyclerView rv_respuestas;
    private TextView tv_timer;
    private TextView tv_numPreguntas;
    private ProgressBar progressBar;
    private List<Preguntas> lstPreguntas;
    private int index = 0;
    private User user;
    private int iLocalScore = 0;
    private int iActualScore;
    private int gemas = 0;
    private int level;
    private CountDownTimer timer;
    private int segundos = 0;
    private int puntos = 0;
    private int iCorrectas = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);
        init();
    }

    public void init(){
        context = this;
        presenter = new PreguntasPresenter(new PreguntasInteractor(new QuizClient()));
        presenter.setView(this);
        tv_pregunta = findViewById(R.id.tv_pregunta);
        rv_respuestas = findViewById(R.id.rv_respuestas);
        progressBar = findViewById(R.id.pb_login);
        tv_timer = findViewById(R.id.tv_timer);
        tv_numPreguntas = findViewById(R.id.numPreguntas);
        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");
        int iIdAnime = bundle.getInt("idAnime");
        level = bundle.getInt("level");
        iActualScore = bundle.getInt("score");
        setupRecyclerView();
        if (UtilInternetConnection.isOnline(context())){
            if (null != user) {
                presenter.getQuestionsByAnimeAndLevel(user.getUserName(),user.getPassword(),iIdAnime,level);
            } else {
                Toast.makeText(context(),"Ocurrio un error",Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context(),getString(R.string.noInternet),Toast.LENGTH_LONG).show();
        }
    }

    private void setupRecyclerView(){
        RespuestasAdapter adapter = new RespuestasAdapter();
        adapter.setItemClickListener((Respuesta respuesta) -> {
            presenter.calculaPuntos(respuesta);
        });
        rv_respuestas.setLayoutManager(new LinearLayoutManager(this));
        rv_respuestas.setHasFixedSize(true);
        rv_respuestas.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        presenter.terminate();
        super.onDestroy();
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
    public void showQuestionsNotFoundMessage() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showServerError(String error) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(context(),getString(R.string.serverError,error),Toast.LENGTH_LONG).show();
    }

    @Override
    public void renderQuestions(List<Preguntas> lstPreguntas) {
        this.lstPreguntas = lstPreguntas;
        nextQuestion();
    }

    @Override
    public void calcularPuntos(Respuesta respuesta) {
        if (respuesta.getIsCorrect().equals(TRUE)){
            iLocalScore += (puntos * segundos);
            iCorrectas++;
        }
        nextQuestion();
    }

    @Override
    public Context context() {
        return context;
    }

    public void nextQuestion(){
        resetTimer();
        starTime();

        if (index < lstPreguntas.size()){
            Preguntas pregunta = lstPreguntas.get(index);
            puntos = pregunta.getPuntos();
            RespuestasAdapter adapter = new RespuestasAdapter();
            adapter.setItemClickListener((Respuesta respuesta) -> {
                presenter.calculaPuntos(respuesta);
            });
            adapter.setLstRespuesta(pregunta.getArrayRespuestas());
            rv_respuestas.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            tv_pregunta.setText(pregunta.getQuestion());
            String sNumPreguntas = (index + 1) + "/" + lstPreguntas.size();
            tv_numPreguntas.setText(sNumPreguntas);
            index++;
        } else {
            //Mostrar Resultados
            finish();
        }
    }

    public void starTime(){
        timer = new CountDownTimer(TIME_QUESTIONS,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_timer.setText(getString(R.string.timer,String.valueOf(millisUntilFinished / 1000)));
                segundos = (int) (millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                //Next Question
            }
        }.start();
    }

    public void resetTimer(){
        if (timer != null) {
            timer.cancel();
            timer = null;
            segundos = 0;
            tv_timer.setText(getString(R.string.timer, "10"));
        }
    }
}
