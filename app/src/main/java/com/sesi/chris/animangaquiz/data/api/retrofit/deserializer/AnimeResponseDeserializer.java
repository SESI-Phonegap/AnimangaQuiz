package com.sesi.chris.animangaquiz.data.api.retrofit.deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.sesi.chris.animangaquiz.data.api.Constants;
import java.lang.reflect.Type;
import java.util.List;

public class AnimeResponseDeserializer<T> implements ListDeserializer<T> {

    @Override
    public List<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject animeJsonObject = json.getAsJsonObject().get(Constants.Deserializer.ANIMES).getAsJsonObject();
        JsonElement animesJsonArray = animeJsonObject.getAsJsonArray();
        return new Gson().fromJson(animesJsonArray, typeOfT);
    }
}
