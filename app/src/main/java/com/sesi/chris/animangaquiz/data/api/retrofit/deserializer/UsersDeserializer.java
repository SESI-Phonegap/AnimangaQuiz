package com.sesi.chris.animangaquiz.data.api.retrofit.deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sesi.chris.animangaquiz.data.api.Constants;

import java.lang.reflect.Type;
import java.util.List;

public class UsersDeserializer<T> implements ListDeserializer<T>  {

    @Override
    public List<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray friendsJsonArray = json.getAsJsonObject().get(Constants.Deserializer.FRIENDS).getAsJsonArray();
        return new Gson().fromJson(friendsJsonArray, typeOfT);
    }
}
