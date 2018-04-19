package com.sesi.chris.animangaquiz.data.api.retrofit.deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class CheckLevelAndScoreDeserializer<T> implements ObjDeserializer<T> {
    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement ScoreJsonObj = json.getAsJsonObject();
        return new Gson().fromJson(ScoreJsonObj, typeOfT);
    }
}
