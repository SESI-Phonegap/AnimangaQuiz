package com.sesi.chris.animangaquiz.view.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
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

public class PreguntasActivity extends AppCompatActivity implements PreguntasPresenter.ViewPreguntas {

    private static final String TRUE = "1";
    private static final int TIME_QUESTIONS = 16000;
    private static final int FACIL = 1;
    private static final int MEDIO = 2;
    private static final int DIFICIL = 3;
    private static final int OTAKU = 4;
    private Context context;
    private PreguntasPresenter presenter;
    private TextView tvPregunta;
    private RecyclerView rvRespuestas;
    private TextView tvTimer;
    private TextView tvNumPreguntas;
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
    private InterstitialAd mInterstitialAd;
    private AdView mAdview;
    private TextView tvDialogGemas;
    private RewardedAd mRewardedAd;
    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);
        init();
    }

    private void init() {
        context = this;
        MobileAds.initialize(getApplicationContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

        presenter = new PreguntasPresenter(new PreguntasInteractor(new QuizClient()));
        presenter.setView(this);
        tvPregunta = findViewById(R.id.tv_pregunta);
        rvRespuestas = findViewById(R.id.rv_respuestas);
        progressBar = findViewById(R.id.pb_login);
        tvTimer = findViewById(R.id.tv_timer);
        tvNumPreguntas = findViewById(R.id.numPreguntas);
        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");
        iIdAnime = bundle.getInt("idAnime");
        level = bundle.getInt("level");
        iActualScore = bundle.getInt("score");
        setupRecyclerView();
        if (UtilInternetConnection.isOnline(context())) {
            // Use an activity context to get the rewarded video instance.
            loadAdReward();
            cargarInterstitial();
            if (null != user) {
                presenter.getQuestionsByAnimeAndLevel(user.getEmail(), user.getPassword(), iIdAnime, level);
            } else {
                Toast.makeText(context(), "Ocurrio un error", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context(), getString(R.string.noInternet), Toast.LENGTH_LONG).show();
        }
    }

    private void loadAdReward(){
        adRequest = new AdRequest.Builder().build();
        RewardedAd.load(getApplicationContext(),getString(R.string.bannerBonificacion), adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                mRewardedAd = rewardedAd;
                super.onAdLoaded(rewardedAd);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mRewardedAd = null;
                super.onAdFailedToLoad(loadAdError);
            }
        });
    }

    private void showRewardedVideo(){
        if (null != mRewardedAd){
            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    // Called when ad fails to show.
                    Log.d("Ad", "Ad failed to show.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    // Called when ad is shown.
                    Log.d("Ad","Ad was shown.");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    Log.d("Ad", "Ad was dismissed.");
                    mRewardedAd = null;
                }
            });
            mRewardedAd.show(this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    gemas += rewardItem.getAmount();
                    tvDialogGemas.setText(String.valueOf(gemas));
                }
            });
        }

    }

    private void setupRecyclerView() {
        RespuestasAdapter adapter = new RespuestasAdapter();
        adapter.setItemClickListener((Respuesta respuesta) -> presenter.calculaPuntos(respuesta));
        rvRespuestas.setLayoutManager(new LinearLayoutManager(this));
        rvRespuestas.setHasFixedSize(true);
        rvRespuestas.setAdapter(adapter);
    }

    private void cargarInterstitial(){
        mAdview = findViewById(R.id.adView);
        mAdview.loadAd(adRequest);
        mAdview.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mAdview.loadAd(new AdRequest.Builder().build());
            }
        });
        InterstitialAd.load(getApplicationContext(), getString(R.string.interstitialId), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }
        });
    }

    public void showInterstitialAd(){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    @Override
    protected void onDestroy() {
        presenter.terminate();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
    public void renderUpdateEsferas(UpdateResponse updateResponse) {
        Log.d("UPDATE-ESFERAS--",updateResponse.error);
        Toast.makeText(context(),updateResponse.error,Toast.LENGTH_LONG).show();
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
            rvRespuestas.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            tvPregunta.setText(pregunta.getQuestion());
            String sNumPreguntas = (index + 1) + "/" + lstPreguntas.size();
            tvNumPreguntas.setText(sNumPreguntas);
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
                tvTimer.setText(getString(R.string.timer, String.valueOf(millisUntilFinished / 1000)));
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
            tvTimer.setText(getString(R.string.timer, "10"));
        }
    }

    private void showDialogResultados() {
        showInterstitialAd();
        AlertDialog.Builder builder = new AlertDialog.Builder(context());
        final View view = getLayoutInflater().inflate(R.layout.dialog_resultados, null);
        TextView tvDialogpCorrectas = view.findViewById(R.id.tv_pCorrectas);
        TextView tvDialogPuntos = view.findViewById(R.id.tv_puntos);
        tvDialogGemas = view.findViewById(R.id.tv_dialog_gemas);
        TextView tvDialgoNewRecord = view.findViewById(R.id.tv_newRecord);
        TextView tvBtnAnuncio = view.findViewById(R.id.tv_btn_anuncio);
        Button btnDialogAceptar = view.findViewById(R.id.btn_aceptar);

        String sPreguntasCorrectas = iCorrectas + "/" + lstPreguntas.size();
        tvDialogpCorrectas.setText(sPreguntasCorrectas);
        tvDialogPuntos.setText(String.valueOf(iLocalScore));

        //--- Calcular cuantas gemas obtienes
        gemas = calculaGemas(iLocalScore, level);
        tvDialogGemas.setText(String.valueOf(gemas));

        int iscoreAux;
        // --- Nuevo Record ----
        if (iLocalScore > iActualScore) {
            tvDialgoNewRecord.setVisibility(View.VISIBLE);
            AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
            animation1.setDuration(10000);
            animation1.setRepeatCount(20);
            tvDialgoNewRecord.startAnimation(animation1);
            iscoreAux = iLocalScore;
        } else {
            iscoreAux = iActualScore;
        }

        int finalIscoreAux = iscoreAux;
        btnDialogAceptar.setOnClickListener(v -> {
            dialog.dismiss();
            updataLevelScoreGems(user.getEmail(),user.getPassword(),gemas, finalIscoreAux,level,user.getIdUser(),iIdAnime);
        });

        tvBtnAnuncio.setOnClickListener(v -> {
            showRewardedVideo();
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void updataLevelScoreGems(String userName, String pass, int gems, int score, int level,int idUser, int idAnime) {
        if (UtilInternetConnection.isOnline(context())) {
            presenter.updateLevelSocreGems(userName,pass,gems,score,level,idUser,idAnime);
        } else {
            Toast.makeText(context(), getString(R.string.noInternet), Toast.LENGTH_LONG).show();
        }
    }

    private int getGemasFacil(int puntos){
        int iGemas = 0;
        if (puntos >= 100 && puntos <= 999) {
            iGemas = getResources().getInteger(R.integer.facilMin);
        } else if (puntos >= 1000 && puntos <= 2000) {
            iGemas = getResources().getInteger(R.integer.facilMed);
        } else if (puntos >= 2001 && puntos <= 3000) {
            iGemas = getResources().getInteger(R.integer.facilMax);
            if (user.getEsferas() < 2){
                presenter.updateEsferas(user.getEmail(),user.getPassword(),user.getIdUser(),user.getEsferas() + 1);
            }
        }
        return iGemas;
    }

    private int getGemasMedio(int puntos){
        int iGemas = 0;
        if (puntos >= 100 && puntos <= 2000) {
            iGemas = getResources().getInteger(R.integer.medMin);
        } else if (puntos >= 2001 && puntos <= 4000) {
            iGemas = getResources().getInteger(R.integer.medMed);
        } else if (puntos >= 4001 && puntos <= 6000) {
            iGemas = getResources().getInteger(R.integer.medMax);
            if (user.getEsferas() < 4 && user.getEsferas() >=2){
                presenter.updateEsferas(user.getEmail(),user.getPassword(),user.getIdUser(),user.getEsferas() + 1);
            }
        }
        return iGemas;
    }

    private int getGemasDificil(int puntos){
        int iGemas = 0;
        if (puntos >= 100 && puntos <= 3000) {
            iGemas = getResources().getInteger(R.integer.dificilMin);
        } else if (puntos >= 3001 && puntos <= 6000) {
            iGemas = getResources().getInteger(R.integer.dificilMed);
        } else if (puntos >= 6001 && puntos <= 9000) {
            iGemas = getResources().getInteger(R.integer.dificilMax);
            if (user.getEsferas() < 6 && user.getEsferas() >=4){
                presenter.updateEsferas(user.getEmail(),user.getPassword(),user.getIdUser(),user.getEsferas() + 1);
            }
        }
        return iGemas;
    }

    private int getGemasOtaku(int puntos){
        int iGemas = 0;
        if (puntos >= 100 && puntos <= 4500) {
            iGemas = getResources().getInteger(R.integer.otakuMin);
        } else if (puntos >= 4501 && puntos <= 9000) {
            iGemas = getResources().getInteger(R.integer.otakuMed);
        } else if (puntos >= 9001 && puntos <= 13500) {
            iGemas = getResources().getInteger(R.integer.otakuMax);
            if (user.getEsferas() < 7 && user.getEsferas() >=6){
                presenter.updateEsferas(user.getEmail(),user.getPassword(),user.getIdUser(),user.getEsferas() + 1);
            }
        }
        return iGemas;
    }

    private int calculaGemas(int puntos, int level) {
        int iGemas = 0;
        switch (level) {
            case FACIL:
                iGemas = getGemasFacil(puntos);
                break;
            case MEDIO:
                iGemas = getGemasMedio(puntos);
                break;

            case DIFICIL:
                iGemas = getGemasDificil(puntos);
                break;

            case OTAKU:
                iGemas = getGemasOtaku(puntos);
                break;
            default:
                Toast.makeText(context(),R.string.noValid,Toast.LENGTH_LONG).show();
                break;

        }
        return iGemas;
    }
}
