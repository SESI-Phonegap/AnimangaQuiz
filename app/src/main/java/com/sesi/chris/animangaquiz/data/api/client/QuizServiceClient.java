package com.sesi.chris.animangaquiz.data.api.client;


import com.sesi.chris.animangaquiz.data.model.LoginResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;

public interface QuizServiceClient {
    Observable<LoginResponse> login(String userName, String pass);
}
