package com.zflabs.marveldatabase.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zflabs.marveldatabase.data.Character;
import com.zflabs.marveldatabase.data.Comic;
import com.zflabs.marveldatabase.data.Result;

import java.lang.reflect.Type;
import java.util.List;

public final class MarvelDBJsonUtils {

    public static List<Character> getCharacterListFromJson(String json){
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Result<Character>>() {}.getType();
        Result<Character> result  = gson.fromJson(json, collectionType);
        return result.getData().getResults();
    }

    public static List<Comic> getComicListFromJson(String json){
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Result<Comic>>() {}.getType();
        Result<Comic> result  = gson.fromJson(json, collectionType);
        return result.getData().getResults();
    }
}