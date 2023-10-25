package com.sesi.chris.animangaquiz.view.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.Constants;
import com.sesi.chris.animangaquiz.data.api.client.QuizClient;
import com.sesi.chris.animangaquiz.data.model.Preguntas;
import com.sesi.chris.animangaquiz.data.model.Respuesta;
import com.sesi.chris.animangaquiz.data.model.UpdateResponse;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.interactor.PreguntasImgInteractor;
import com.sesi.chris.animangaquiz.presenter.PreguntasImgPresenter;
import com.sesi.chris.animangaquiz.view.adapter.RespuestasAdapter;
import com.sesi.chris.animangaquiz.view.utils.UtilInternetConnection;

import java.util.List;

public class PreguntasImgActivity extends AppCompatActivity implements PreguntasImgPresenter.ViewPreguntas {

    private ProgressBar pb;
    private TextView tvNumeroPreguntas;
    private TextView tvTiempo;
    private ImageView imgQuiz;
    private RecyclerView rvRespuestas;
    private AdView adView;
    private List<Preguntas> lstPreguntas;
    private InterstitialAd mInterstitialAd;
    private Context context;
    private PreguntasImgPresenter presenter;
    private User user;
    private int iIdAnime;
    private int iLocalScore = 0;
    private int iActualScore;
    private int segundos = 0;
    private int puntos = 0;
    private int iCorrectas = 0;
    private int index = 0;
    private CountDownTimer timer;
    private TextView tvDialogGemas;
    private int gemas = 0;
    private AlertDialog dialog;
    private static final String TRUE = "1";
    private static final int TIME_QUESTIONS = 16000;
    private AdRequest adRequest;
    private RewardedAd mRewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas_img);
        init();
    }

    public void init(){
        context = this;
        presenter = new PreguntasImgPresenter(new PreguntasImgInteractor(new QuizClient()));
        presenter.setView(this);
        pb = findViewById(R.id.pb_quiz);
        tvNumeroPreguntas = findViewById(R.id.numPreguntas);
        tvTiempo = findViewById(R.id.tvTiempo);
        imgQuiz = findViewById(R.id.imgQuiz);
        rvRespuestas = findViewById(R.id.rvRespuesta);
        adView = findViewById(R.id.adView);
        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");
        iIdAnime = bundle.getInt("idAnime");
        iActualScore = bundle.getInt("score");
        setupRecyclerView();
        if (UtilInternetConnection.isOnline(context())) {
            loadAdReward();
            cargarInterstitial();
            if (null != user) {
                presenter.getQuestionsByAnimeImg(user.getEmail(), user.getPassword(), iIdAnime);
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
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mRewardedAd = null;
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

    private void cargarInterstitial(){
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                adView.loadAd(new AdRequest.Builder().build());
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

    private void setupRecyclerView() {
        RespuestasAdapter adapter = new RespuestasAdapter();
        adapter.setItemClickListener((Respuesta respuesta) -> presenter.calculaPuntos(respuesta));
        rvRespuestas.setLayoutManager(new LinearLayoutManager(this));
        rvRespuestas.setHasFixedSize(true);
        rvRespuestas.setAdapter(adapter);
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
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pb.setVisibility(View.GONE);
    }

    @Override
    public void showQuestionsNotFoundMessage() {
        pb.setVisibility(View.GONE);
    }

    @Override
    public void showServerError(String error) {
        pb.setVisibility(View.GONE);
        Toast.makeText(context(), getString(R.string.serverError, error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void renderQuestions(List<Preguntas> lstPreguntas) {
        this.lstPreguntas = lstPreguntas;
        nextQuestion();
    }

    @Override
    public void calcularPuntos(Respuesta respuesta) {
        pb.setVisibility(View.VISIBLE);
        imgQuiz.clearColorFilter();
        if (respuesta.getIsCorrect().equals(TRUE)) {
            iLocalScore += (puntos * segundos);
            iCorrectas++;
        }
        Handler handler = new Handler();
        Runnable runnable = this::nextQuestion;
        handler.postDelayed(runnable,500);
    }

    @Override
    public void renderUpdateEsferas(UpdateResponse updateResponse) {
        Log.d("UPDATE-ESFERAS--",updateResponse.error);
        Toast.makeText(context(),updateResponse.error,Toast.LENGTH_LONG).show();
    }

    @Override
    public void renderUpdateScoreLevelGems(UpdateResponse updateResponse) {
        Log.d("UPDATE--",updateResponse.error);
        finish();
    }

    private void nextQuestion() {
        resetTimer();


        if (index < lstPreguntas.size()) {
            Preguntas pregunta = lstPreguntas.get(index);
            puntos = pregunta.getPuntos();
            Glide.with(context).load(Constants.URL_BASE+"/"+pregunta.getQuestion()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                    //imgQuiz.setImageBitmap(redimensionImgMax(((BitmapDrawable)resource).getBitmap(),100f,100f));
                    imgQuiz.setImageDrawable(resource);
                    imgQuiz.setColorFilter(R.color.black, PorterDuff.Mode.MULTIPLY);
                    RespuestasAdapter adapter = new RespuestasAdapter();
                    adapter.setItemClickListener((Respuesta respuesta) -> presenter.calculaPuntos(respuesta));
                    adapter.setLstRespuesta(pregunta.getArrayRespuestas());
                    rvRespuestas.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    String sNumPreguntas = (index + 1) + "/" + lstPreguntas.size();
                    tvNumeroPreguntas.setText(sNumPreguntas);
                    index++;
                    pb.setVisibility(View.GONE);
                    starTime();
                    return false;
                }
            }).into(imgQuiz);
          /*  Picasso.get()
                    .load(Constants.URL_BASE+"/"+pregunta.getQuestion())
                    .into(new Target() {

                        @Override
                        public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from) {
                            imgQuiz.setImageBitmap(redimensionImgMax(bitmap,100f,100f));
                            imgQuiz.setColorFilter(R.color.black, PorterDuff.Mode.MULTIPLY);
                            RespuestasAdapter adapter = new RespuestasAdapter();
                            adapter.setItemClickListener((Respuesta respuesta) -> presenter.calculaPuntos(respuesta));
                            adapter.setLstRespuesta(pregunta.getArrayRespuestas());
                            rvRespuestas.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            String sNumPreguntas = (index + 1) + "/" + lstPreguntas.size();
                            tvNumeroPreguntas.setText(sNumPreguntas);
                            index++;
                            pb.setVisibility(View.GONE);
                            starTime();
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            //Empty Method
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                            //Empty Method
                        }

                    });
*/
        } else {
            //Mostrar Resultados
            resetTimer();
            showDialogResultados();
        }
    }

    private void resetTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            segundos = 0;
            tvTiempo.setText(getString(R.string.timer, "10"));
        }
    }

    private void starTime() {
        timer = new CountDownTimer(TIME_QUESTIONS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTiempo.setText(getString(R.string.timer, String.valueOf(millisUntilFinished / 1000)));
                segundos = (int) (millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                //Next Question
                nextQuestion();
            }
        }.start();
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
        gemas = calculaGemas(iLocalScore);
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
            updataLevelScoreGems(user.getEmail(),user.getPassword(),gemas, finalIscoreAux,4,user.getIdUser(),iIdAnime);
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

    public int calculaGemas(int puntos){
        int iGemas = 0;
        int iEsferas = user.getEsferas();
        if (puntos >= 100 && puntos <= 2000) {
            iGemas = getResources().getInteger(R.integer.medMin);
        } else if (puntos >= 2001 && puntos <= 4000) {
            iGemas = getResources().getInteger(R.integer.medMed);
        } else if (puntos >= 4001 && puntos <= 6000) {
            iGemas = getResources().getInteger(R.integer.medMax);
            if (iEsferas < 7 && iEsferas >=0){
                presenter.updateEsferas(user.getEmail(),user.getPassword(),user.getIdUser(),user.getEsferas() + 1);
            }
        }
        return iGemas;
    }

    public void updataLevelScoreGems(String userName, String pass, int gems, int score, int level,int idUser, int idAnime) {
        if (UtilInternetConnection.isOnline(context())) {
            presenter.updateLevelSocreGems(userName,pass,gems,score,level,idUser,idAnime);
        } else {
            Toast.makeText(context(), getString(R.string.noInternet), Toast.LENGTH_LONG).show();
        }
    }

    public void showInterstitialAd(){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    @Override
    public Context context() {
        return context;
    }

}
