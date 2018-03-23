package com.sesi.chris.animangaquiz.data.api.client;


import com.sesi.chris.animangaquiz.data.model.LoginResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public interface QuizServiceClient {
    Observable<List<LoginResponse>> login(RequestBody json);
}
