package com.sesi.chris.animangaquiz.data.api.retrofit.deserializer;


import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.sesi.chris.animangaquiz.data.api.Constants;

import java.lang.reflect.Type;
import java.util.List;

public class LoginResponseDeserializer<T> implements ListDeserializer<T>{
    @Override
    public List<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
       // JsonObject loginJsonObject = json.getAsJsonObject().get(Constants.Deserializer.LOGIN).getAsJsonObject();
       // JsonElement loginJsonArray = loginJsonObject.getAsJsonArray(Constants.Deserializer.ITEMS);
        JsonElement loginJsonArray = json.getAsJsonArray();
        return new Gson().fromJson(loginJsonArray, typeOfT);
    }
}
