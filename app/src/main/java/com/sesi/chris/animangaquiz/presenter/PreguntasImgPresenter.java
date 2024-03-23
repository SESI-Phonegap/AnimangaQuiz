package com.sesi.chris.animangaquiz.presenter;

import com.sesi.chris.animangaquiz.data.model.Preguntas;
import com.sesi.chris.animangaquiz.data.model.Respuesta;
import com.sesi.chris.animangaquiz.data.model.UpdateResponseD;
import com.sesi.chris.animangaquiz.interactor.PreguntasImgInteractor;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class PreguntasImgPresenter extends Presenter<PreguntasImgPresenter.ViewPreguntas> {
    private PreguntasImgInteractor interactor;

    public PreguntasImgPresenter(PreguntasImgInteractor interactor){
        this.interactor = interactor;
    }

    public void getQuestionsByAnimeImg(String userName, String pass, int idAnime) {
        getView().showLoading();
        Disposable disposable = interactor.preguntasByAnimeImg(userName, pass, idAnime)
                .doOnError(error -> {
                    getView().hideLoading();
                    getView().showServerError(error.getMessage());
                }).subscribe(preguntas -> {
                    getView().hideLoading();
                    if (!preguntas.isEmpty()){
                        getView().renderQuestions(preguntas);
                    } else {
                        getView().showQuestionsNotFoundMessage();
                    }
                },Throwable::printStackTrace);
        addDisposableObserver(disposable);
    }

    public void updateEsferas(String userName, String pass, int idUser, int esferas){
        getView().showLoading();
        Disposable disposable = interactor.updateEsferas(userName, pass, idUser, esferas)
                .doOnError(error -> {
                    getView().showServerError(error.getMessage());
                    getView().hideLoading();
                }).subscribe(updateResponse -> {
                    getView().hideLoading();
                    getView().renderUpdateEsferas(updateResponse);
                },Throwable::printStackTrace);
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

    public interface ViewPreguntas extends Presenter.View {

        void showLoading();

        void hideLoading();

        void showQuestionsNotFoundMessage();

        void showServerError(String error);

        void renderQuestions(List<Preguntas> lstPreguntas);

        void calcularPuntos(Respuesta respuesta);

        void renderUpdateEsferas(UpdateResponseD updateResponse);

        void renderUpdateScoreLevelGems(UpdateResponseD updateResponse);

    }
}
