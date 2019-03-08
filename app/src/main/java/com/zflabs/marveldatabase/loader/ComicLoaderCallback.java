package com.zflabs.marveldatabase.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;

import com.zflabs.marveldatabase.adapter.ComicAdapter;
import com.zflabs.marveldatabase.data.Comic;

import java.util.List;

public class ComicLoaderCallback implements LoaderManager.LoaderCallbacks<List<Comic>> {

    private int characterId;

    private ComicAdapter comicAdapter;

    private Context context;
    private View mLoadingIndicator;

    public ComicLoaderCallback(Context context, int characterId, ComicAdapter comicAdapter) {
        this.context = context;
        this.characterId = characterId;
        this.comicAdapter = comicAdapter;
    }

    @Override
    public android.support.v4.content.Loader<List<Comic>> onCreateLoader(int id, final Bundle loaderArgs) {
        return new ComicLoader(context, characterId);
    }

    @Override
    public void onLoadFinished(Loader<List<Comic>> loader, List<Comic> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if (null == data) {
        } else {
            comicAdapter.setComicData(data);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<Comic>> loader) {
        loader.cancelLoad();
        loader.reset();
        loader.forceLoad();
    }

    public void setmLoadingIndicator(View mLoadingIndicator) {
        this.mLoadingIndicator = mLoadingIndicator;
    }
}
