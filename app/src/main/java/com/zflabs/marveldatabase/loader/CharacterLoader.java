package com.zflabs.marveldatabase.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.view.View;

import com.zflabs.marveldatabase.data.Character;
import com.zflabs.marveldatabase.util.MarvelDBJsonUtils;
import com.zflabs.marveldatabase.util.NetworkUtils;

import java.net.URL;
import java.util.List;

public class CharacterLoader extends AsyncTaskLoader<java.util.List<Character>> {

    public String searchParam;
    private List<Character> characters = null;
    private int characterId;
    private final View mLoadingIndicator;

    public CharacterLoader(Context context, String searchParam, View mLoadingIndicator){
        super(context);
        this.searchParam = searchParam;
        this.mLoadingIndicator = mLoadingIndicator;
    }

    public CharacterLoader(Context context, int characterId, View mLoadingIndicator){
        super(context);
        this.characterId = characterId;
        this.mLoadingIndicator = mLoadingIndicator;
    }

    @Override
    protected void onStartLoading() {
        if (characters != null) {
            deliverResult(characters);
        } else {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            forceLoad();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        this.characters = null;
    }

    @Override
    public List<Character> loadInBackground() {
        URL movieRequestURL;
        if(searchParam!=null)
            movieRequestURL = NetworkUtils.buildUrl(searchParam);
        else
            movieRequestURL = NetworkUtils.buildUrl(characterId);
        try {
            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);
            List<Character> simpleJsonHeroData = MarvelDBJsonUtils
                    .getCharacterListFromJson(jsonMovieResponse);
            return simpleJsonHeroData;
        } catch (Exception e) {
            Log.e("CharacterLoader", e.getMessage());
            return null;
        }
    }

    public void deliverResult(List<Character> data) {
        characters = data;
        super.deliverResult(data);
    }
}
