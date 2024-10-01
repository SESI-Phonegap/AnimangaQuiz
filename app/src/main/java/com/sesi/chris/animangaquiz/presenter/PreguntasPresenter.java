package com.sesi.chris.animangaquiz.presenter;

import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.QuestionByAnimeLevelRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.UpdateEsferasRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.UpdateLevelScoreGemsTotalScoreRequest;
import com.sesi.chris.animangaquiz.data.model.Preguntas;
import com.sesi.chris.animangaquiz.data.model.Respuesta;
import com.sesi.chris.animangaquiz.data.model.UpdateResponseD;
import com.sesi.chris.animangaquiz.interactor.PreguntasInteractor;
import java.util.List;
import io.reactivex.disposables.Disposable;

public class PreguntasPresenter extends Presenter<PreguntasPresenter.ViewPreguntas> {

    private PreguntasInteractor interactor;

    public PreguntasPresenter(PreguntasInteractor interactor) {
        this.interactor = interactor;
    }

    public void getQuestionsByAnimeAndLevel(String userName, String pass, int idAnime, int level) {
        getView().showLoading();
        QuestionByAnimeLevelRequest request = new QuestionByAnimeLevelRequest(userName, pass, idAnime, level);
        Disposable disposable = interactor.preguntasByAnimeAndLevel(request)
                .doOnError(error -> {
                    getView().showServerError(error.getMessage());
                    getView().hideLoading();
                })
                .subscribe(preguntas -> {
                    if (!preguntas.isEmpty()) {
                        getView().hideLoading();
                        getView().renderQuestions(preguntas);
                    } else {
                        getView().showQuestionsNotFoundMessage();
                    }
                }, Throwable::printStackTrace);
        addDisposableObserver(disposable);
    }

    public void updateLevelSocreGems(String userName, String pass, int gemas, int score, int level, int idUser, int idAnime) {
        getView().showLoading();
        UpdateLevelScoreGemsTotalScoreRequest request = new UpdateLevelScoreGemsTotalScoreRequest(userName, pass, gemas, score, level, idUser, idAnime);
        Disposable disposable = interactor.updateLevelScoreGems(request)
                .doOnError(error -> {
                    getView().showServerError(error.getMessage());
                    getView().hideLoading();
                })
                .subscribe(updateResponse -> {
                    getView().hideLoading();
                    getView().renderUpdateScoreLevelGems(updateResponse);
                },Throwable::printStackTrace);
        addDisposableObserver(disposable);
    }

    public void updateEsferas(String userName, String pass, int idUser, int esferas){
        getView().showLoading();
        UpdateEsferasRequest request = new UpdateEsferasRequest(userName,pass,idUser,esferas);
        Disposable disposable = interactor.updateEsferas(request)
                .doOnError(error -> {
                    getView().showServerError(error.getMessage());
                    getView().hideLoading();
                }).subscribe(updateResponse -> {
                    getView().hideLoading();
                    getView().renderUpdateEsferas(updateResponse);
                },Throwable::printStackTrace);
        addDisposableObserver(disposable);
    }

    public void calculaPuntos(Respuesta respuesta) {
        getView().calcularPuntos(respuesta);
    }

    public interface ViewPreguntas extends Presenter.View {

        void showLoading();

        void hideLoading();

        void showQuestionsNotFoundMessage();

        void showServerError(String error);

        void renderQuestions(List<Preguntas> lstPreguntas);

        void calcularPuntos(Respuesta respuesta);

        void renderUpdateScoreLevelGems(UpdateResponseD updateResponse);

        void renderUpdateEsferas(UpdateResponseD updateResponse);

    }
}
