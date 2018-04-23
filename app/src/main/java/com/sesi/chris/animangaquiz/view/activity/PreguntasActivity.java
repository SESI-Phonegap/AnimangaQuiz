package com.sesi.chris.animangaquiz.view.activity;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.client.QuizClient;
import com.sesi.chris.animangaquiz.data.model.Preguntas;
import com.sesi.chris.animangaquiz.data.model.Respuesta;
import com.sesi.chris.animangaquiz.data.model.UpdateResponse;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.interactor.PreguntasInteractor;
import com.sesi.chris.animangaquiz.presenter.PreguntasPresenter;
import com.sesi.chris.animangaquiz.view.adapter.RespuestasAdapter;
import com.sesi.chris.animangaquiz.view.utils.UtilInternetConnection;

import java.util.List;

public class PreguntasActivity extends AppCompatActivity implements PreguntasPresenter.View {

    private static final String TRUE = "1";
    private static final int TIME_QUESTIONS = 16000;
    private static final int FACIL = 1;
    private static final int MEDIO = 2;
    private static final int DIFICIL = 3;
    private static final int OTAKU = 4;
    private Context context;
    private PreguntasPresenter presenter;
    private TextView tv_pregunta;
    private RecyclerView rv_respuestas;
    private TextView tv_timer;
    private TextView tv_numPreguntas;
    private ProgressBar progressBar;
    private AlertDialog dialog;
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
    private int iIdAnime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);
        init();
    }

    private void init() {
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
        iIdAnime = bundle.getInt("idAnime");
        level = bundle.getInt("level");
        iActualScore = bundle.getInt("score");
        setupRecyclerView();
        if (UtilInternetConnection.isOnline(context())) {
            if (null != user) {
                presenter.getQuestionsByAnimeAndLevel(user.getUserName(), user.getPassword(), iIdAnime, level);
            } else {
                Toast.makeText(context(), "Ocurrio un error", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context(), getString(R.string.noInternet), Toast.LENGTH_LONG).show();
        }
    }

    private void setupRecyclerView() {
        RespuestasAdapter adapter = new RespuestasAdapter();
        adapter.setItemClickListener((Respuesta respuesta) -> presenter.calculaPuntos(respuesta));
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
        Toast.makeText(context(), getString(R.string.serverError, error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void renderQuestions(List<Preguntas> lstPreguntas) {
        this.lstPreguntas = lstPreguntas;
        nextQuestion();
    }

    @Override
    public void calcularPuntos(Respuesta respuesta) {
        if (respuesta.getIsCorrect().equals(TRUE)) {
            iLocalScore += (puntos * segundos);
            iCorrectas++;
        }
        nextQuestion();
    }

    @Override
    public void renderUpdateScoreLevelGems(UpdateResponse updateResponse) {
        Log.d("UPDATE--",updateResponse.error);
        finish();
    }

    @Override
    public Context context() {
        return context;
    }

    private void nextQuestion() {
        resetTimer();
        starTime();

        if (index < lstPreguntas.size()) {
            Preguntas pregunta = lstPreguntas.get(index);
            puntos = pregunta.getPuntos();
            RespuestasAdapter adapter = new RespuestasAdapter();
            adapter.setItemClickListener((Respuesta respuesta) -> presenter.calculaPuntos(respuesta));
            adapter.setLstRespuesta(pregunta.getArrayRespuestas());
            rv_respuestas.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            tv_pregunta.setText(pregunta.getQuestion());
            String sNumPreguntas = (index + 1) + "/" + lstPreguntas.size();
            tv_numPreguntas.setText(sNumPreguntas);
            index++;
        } else {
            //Mostrar Resultados
            resetTimer();
            showDialogResultados();
        }
    }

    private void starTime() {
        timer = new CountDownTimer(TIME_QUESTIONS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_timer.setText(getString(R.string.timer, String.valueOf(millisUntilFinished / 1000)));
                segundos = (int) (millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                //Next Question
                nextQuestion();
            }
        }.start();
    }

    private void resetTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            segundos = 0;
            tv_timer.setText(getString(R.string.timer, "10"));
        }
    }

    private void showDialogResultados() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context());
        final View view = getLayoutInflater().inflate(R.layout.dialog_resultados, null);
        TextView tv_dialog_pCorrectas = view.findViewById(R.id.tv_pCorrectas);
        TextView tv_dialog_puntos = view.findViewById(R.id.tv_puntos);
        TextView tv_dialog_gemas = view.findViewById(R.id.tv_dialog_gemas);
        TextView tv_dialgo_newRecord = view.findViewById(R.id.tv_newRecord);
        Button btn_dialog_aceptar = view.findViewById(R.id.btn_aceptar);

        String sPreguntasCorrectas = iCorrectas + "/" + lstPreguntas.size();
        tv_dialog_pCorrectas.setText(sPreguntasCorrectas);
        tv_dialog_puntos.setText(String.valueOf(iLocalScore));

        //--- Calcular cuantas gemas obtienes
        gemas = calculaGemas(iLocalScore, level);
        tv_dialog_gemas.setText(String.valueOf(gemas));

        int iscoreAux;
        // --- Nuevo Record ----
        if (iLocalScore > iActualScore) {
            tv_dialgo_newRecord.setVisibility(View.VISIBLE);
            AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
            animation1.setDuration(10000);
            animation1.setRepeatCount(20);
            tv_dialgo_newRecord.startAnimation(animation1);
            iscoreAux = iLocalScore;
        } else {
            iscoreAux = iActualScore;
        }

        int finalIscoreAux = iscoreAux;
        btn_dialog_aceptar.setOnClickListener(v -> {
            dialog.dismiss();
            updataLevelScoreGems(user.getUserName(),user.getPassword(),gemas, finalIscoreAux,level,user.getIdUser(),iIdAnime);
            //finish();
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void updataLevelScoreGems(String userName, String pass, int gems, int score, int level,int idUser, int idAnime) {
        if (UtilInternetConnection.isOnline(context())) {
            presenter.updateLevelSocreGems(userName,pass,gems,score,level,idUser,idAnime);
        } else {
            Toast.makeText(context(), getString(R.string.noInternet), Toast.LENGTH_LONG).show();
        }
    }

    private int calculaGemas(int puntos, int level) {
        int gemas = 0;
        switch (level) {
            case FACIL:
                if (puntos >= 81 && puntos <= 160) {
                    gemas = getResources().getInteger(R.integer.facilMin);
                } else if (puntos >= 161 && puntos <= 240) {
                    gemas = getResources().getInteger(R.integer.facilMed);
                } else if (puntos >= 241 && puntos <= 320) {
                    gemas = getResources().getInteger(R.integer.facilMax);
                }
                break;
            case MEDIO:
                if (puntos >= 161 && puntos <= 320) {
                    gemas = getResources().getInteger(R.integer.medMin);
                } else if (puntos >= 321 && puntos <= 480) {
                    gemas = getResources().getInteger(R.integer.medMed);
                } else if (puntos >= 481 && puntos <= 640) {
                    gemas = getResources().getInteger(R.integer.medMax);
                }
                break;

            case DIFICIL:
                if (puntos >= 241 && puntos <= 480) {
                    gemas = getResources().getInteger(R.integer.dificilMin);
                } else if (puntos >= 481 && puntos <= 720) {
                    gemas = getResources().getInteger(R.integer.dificilMed);
                } else if (puntos >= 721 && puntos <= 960) {
                    gemas = getResources().getInteger(R.integer.dificilMax);
                }
                break;

            case OTAKU:
                if (puntos >= 321 && puntos <= 640) {
                    gemas = getResources().getInteger(R.integer.otakuMin);
                } else if (puntos >= 641 && puntos <= 960) {
                    gemas = getResources().getInteger(R.integer.otakuMed);
                } else if (puntos >= 961 && puntos <= 1280) {
                    gemas = getResources().getInteger(R.integer.otakuMax);
                }
                break;
        }
        return gemas + user.getCoins();
    }
}
