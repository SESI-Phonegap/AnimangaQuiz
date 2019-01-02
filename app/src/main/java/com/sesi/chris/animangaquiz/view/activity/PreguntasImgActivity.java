package com.sesi.chris.animangaquiz.view.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.util.List;

public class PreguntasImgActivity extends AppCompatActivity implements PreguntasImgPresenter.ViewPreguntas,RewardedVideoAdListener {

    private ProgressBar pb;
    private TextView tvNumeroPreguntas;
    private TextView tvTiempo;
    private ImageView imgQuiz;
    private RecyclerView rvRespuestas;
    private AdView adView;
    private List<Preguntas> lstPreguntas;
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;
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
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        mRewardedVideoAd.loadAd(getString(R.string.bannerBonificacion),
                new AdRequest.Builder().build());
        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");
        iIdAnime = bundle.getInt("idAnime");
        iActualScore = bundle.getInt("score");
        setupRecyclerView();
        if (UtilInternetConnection.isOnline(context())) {
            cargarInterstitial();
            if (null != user) {
                presenter.getQuestionsByAnimeImg(user.getUserName(), user.getPassword(), iIdAnime);
            } else {
                Toast.makeText(context(), "Ocurrio un error", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context(), getString(R.string.noInternet), Toast.LENGTH_LONG).show();
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
        mInterstitialAd = new InterstitialAd(context());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitialId));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
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
        mRewardedVideoAd.destroy(this);
        presenter.terminate();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mRewardedVideoAd.pause(this);
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
        nextQuestion();
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
            Picasso.get()
                    .load(Constants.URL_BASE+pregunta.getQuestion())
                    .into(new Target() {

                        @Override
                        public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from) {
                            /* Save the bitmap or do something with it here */

                            // Set it in the ImageView
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
        } else {
            //Mostrar Resultados
            resetTimer();
            showDialogResultados();
        }
    }

    public Bitmap redimensionImgMax(Bitmap bitmap, float newWidth, float newHeight){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //float scaleWidth = newWidth / width;
        //float scaleHeight = newHeight / height;
       // Matrix matrix = new Matrix();
       // matrix.postScale(scaleWidth,scaleHeight);
        return Bitmap.createBitmap(bitmap,0,0,width,height);
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
            updataLevelScoreGems(user.getUserName(),user.getPassword(),gemas, finalIscoreAux,4,user.getIdUser(),iIdAnime);
        });

        tvBtnAnuncio.setOnClickListener(v -> {
            if (mRewardedVideoAd.isLoaded()) {
                mRewardedVideoAd.show();
            }
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
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
            if (iEsferas < 4 && iEsferas >=0){
                presenter.updateEsferas(user.getUserName(),user.getPassword(),user.getIdUser(),user.getEsferas() + 1);
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
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    @Override
    public Context context() {
        return context;
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        //Empty Method
    }

    @Override
    public void onRewardedVideoAdOpened() {
        //Empty Method
    }

    @Override
    public void onRewardedVideoStarted() {
        //Empty Method
    }

    @Override
    public void onRewardedVideoAdClosed() {
        //Empty Method
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        //Empty Method
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        //Empty Method
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        //Empty Method
    }

    @Override
    public void onRewardedVideoCompleted() {
        gemas += getResources().getInteger(R.integer.bono);
        tvDialogGemas.setText(String.valueOf(gemas));
    }
}
