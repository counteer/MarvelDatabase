package com.zflabs.marveldatabase.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.view.View;

import com.zflabs.marveldatabase.data.Comic;
import com.zflabs.marveldatabase.util.MarvelDBJsonUtils;
import com.zflabs.marveldatabase.util.NetworkUtils;

import java.net.URL;
import java.util.List;

public class ComicLoader extends AsyncTaskLoader<List<Comic>> {

    private List<Comic> comics = null;
    private int characterId;

    public void setmLoadingIndicator(View mLoadingIndicator) {
        this.mLoadingIndicator = mLoadingIndicator;
    }

    private View mLoadingIndicator;

    public ComicLoader(Context context, int characterId){
        super(context);
        this.characterId = characterId;
    }

    @Override
    protected void onStartLoading() {
        if (comics != null) {
            deliverResult(comics);
        } else {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            forceLoad();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        this.comics = null;
    }

    @Override
    public List<Comic> loadInBackground() {
        URL comicRequestUrl;
        comicRequestUrl = NetworkUtils.buildUrlForComics(characterId);
        try {
            String jsonComicResponse = NetworkUtils.getResponseFromHttpUrl(comicRequestUrl);
            List<Comic> simpleJsonComicData = MarvelDBJsonUtils
                    .getComicListFromJson(jsonComicResponse);
            return simpleJsonComicData;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage());
            return null;
        }
    }

    public void deliverResult(List<Comic> data) {
        comics = data;
        super.deliverResult(data);
    }
}
