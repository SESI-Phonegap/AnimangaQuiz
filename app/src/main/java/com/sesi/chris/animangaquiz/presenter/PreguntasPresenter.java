package com.sesi.chris.animangaquiz.presenter;

import com.sesi.chris.animangaquiz.data.model.Preguntas;
import com.sesi.chris.animangaquiz.data.model.Respuesta;
import com.sesi.chris.animangaquiz.data.model.UpdateResponse;
import com.sesi.chris.animangaquiz.interactor.PreguntasInteractor;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class PreguntasPresenter extends Presenter<PreguntasPresenter.View> {

    private PreguntasInteractor interactor;

    public PreguntasPresenter(PreguntasInteractor interactor) {
        this.interactor = interactor;
    }

    public void getQuestionsByAnimeAndLevel(String userName, String pass, int idAnime, int level) {
        getView().showLoading();
        Disposable disposable = interactor.preguntasByAnimeAndLevel(userName, pass, idAnime, level)
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
        Disposable disposable = interactor.updateLevelScoreGems(userName, pass, gemas, score, level, idUser, idAnime)
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

    public void calculaPuntos(Respuesta respuesta) {
        getView().calcularPuntos(respuesta);
    }

    public interface View extends Presenter.View {

        void showLoading();

        void hideLoading();

        void showQuestionsNotFoundMessage();

        void showServerError(String error);

        void renderQuestions(List<Preguntas> lstPreguntas);

        void calcularPuntos(Respuesta respuesta);

        void renderUpdateScoreLevelGems(UpdateResponse updateResponse);

    }
}
