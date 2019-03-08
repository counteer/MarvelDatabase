package com.zflabs.marveldatabase;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zflabs.marveldatabase.adapter.ComicAdapter;
import com.zflabs.marveldatabase.data.Character;
import com.zflabs.marveldatabase.loader.CharacterLoader;
import com.zflabs.marveldatabase.loader.ComicLoader;
import com.zflabs.marveldatabase.loader.ComicLoaderCallback;
import com.zflabs.marveldatabase.persistence.FavoritesContract;
import com.zflabs.marveldatabase.widget.FavoritesService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CharacterActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Character>> {

    private Loader<List<Character>> listLoader;
    private ComicLoader comicListLoader;


    private static int CHARACTER_LOADER_ID = 2213;
    private static final int COMIC_LOADER_ID = 2216;

    private int characterId;

    private ComicAdapter comicAdapter;

    private Character character;

    @BindView(R.id.character_profile)
    ImageView characterProfile;

    @BindView(R.id.character_name)
    TextView characterName;

    @BindView(R.id.character_description)
    TextView characterDescription;

    @BindView(R.id.comics_list)
    RecyclerView comicsList;

    @BindView(R.id.character_profile_loading)
    View characterProfileLoadingIndicator;

    @BindView(R.id.character_comic_list_loading)
    View comicListLoadingIndicator;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.character_context_menu)
    View contextMenu;

    @BindView(R.id.add_character_favorites)
    TextView addToFavoritesButton;

    @BindView(R.id.my_char_toolbar)
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);
        ButterKnife.bind(this);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null && intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)){
            characterId = intentThatStartedThisActivity.getIntExtra(Intent.EXTRA_TEXT,0);
            performSearch();
            comicAdapter = new ComicAdapter();
            comicsList.setLayoutManager(new GridLayoutManager(this, 3));
            comicsList.setAdapter(comicAdapter);
            performComicSearch(characterId);
            validateFavorite();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeMenuVisibility();
                }
            });
            addToFavoritesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modFavorite();
                }
            });
        }
    }

    private void changeMenuVisibility() {
        int visible = contextMenu.getVisibility();
        if(visible==View.VISIBLE) {
            contextMenu.animate()
                    .alpha(0.0f)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            contextMenu.setVisibility(View.GONE);
                        }
                    });
        } else {
            contextMenu.animate()
                    .alpha(1.0f)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            contextMenu.setVisibility(View.VISIBLE);
                        }
                    });
        }
    }

    private void validateFavorite() {
        Log.i("CharacterActivity" , "fav? " + isFavorite());
        if(isFavorite()) {
            addToFavoritesButton.setText(R.string.remove_from_favorites);
        } else {
            addToFavoritesButton.setText(R.string.add_to_favorites);
        }
    }

    private void modFavorite(){
        if(isFavorite()){
            removeFromFavorites();
        } else{
            addToFavorites();
        }
        Toast.makeText(this, R.string.favorite_succeded, Toast.LENGTH_LONG);
        validateFavorite();
        changeMenuVisibility();
    }
    private void addToFavorites(){
        ContentValues cv = new ContentValues();
        cv.put(FavoritesContract.FavoriteEntry.COLUMN_CHARACTER_ID, character.getId());
        cv.put(FavoritesContract.FavoriteEntry.COLUMN_CHARACTER_NAME, character.getName());
        cv.put(FavoritesContract.FavoriteEntry.COLUMN_CHARACTER_IMAGE_PATH, character.getThumbnail().getPath());
        cv.put(FavoritesContract.FavoriteEntry.COLUMN_CHARACTER_IMAGE_EXT, character.getThumbnail().getExtension());
        getContentResolver().insert(FavoritesContract.BASE_CONTENT_URI, cv);
        FavoritesService.startActionListModified(getApplicationContext());
    }

    private boolean isFavorite(){
        Cursor query = getContentResolver().query(FavoritesContract.BASE_CONTENT_URI, new String[]{FavoritesContract.FavoriteEntry.COLUMN_CHARACTER_ID}, FavoritesContract.FavoriteEntry.COLUMN_CHARACTER_ID+" = " + characterId, null, null);
        return query.getCount() >= 1;
    }

    private void removeFromFavorites() {
        getContentResolver().delete(FavoritesContract.BASE_CONTENT_URI, FavoritesContract.FavoriteEntry.COLUMN_CHARACTER_ID + " = " + characterId, null);
    }


    private void performSearch() {
        final int loaderId = CHARACTER_LOADER_ID;
        final LoaderManager.LoaderCallbacks<List<Character>> callback = CharacterActivity.this;
        final Bundle bundleForLoader = null;
        if(listLoader == null)
            listLoader = getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);
        listLoader.reset();
        listLoader.forceLoad();
    }

    @Override
    public Loader<List<Character>> onCreateLoader(int id, final Bundle loaderArgs) {
        return new CharacterLoader(this, characterId, characterProfileLoadingIndicator);
    }

    @Override
    public void onLoadFinished(Loader<List<Character>> loader, List<Character> data) {
        characterProfileLoadingIndicator.setVisibility(View.GONE);
        if(data!=null && data.size()>0) {
            character = data.get(0);
            Picasso.with(this).load(character.getThumbnailUrl()).into(characterProfile);
            characterName.setText(character.getName());
            characterDescription.setText(character.getDescription());
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Character>> loader) {
        loader.cancelLoad();
        loader.reset();
        loader.forceLoad();
    }

    private void performComicSearch(int characterId) {
        final int comicLoaderId = COMIC_LOADER_ID;
        final ComicLoaderCallback callback = new ComicLoaderCallback(this, characterId, comicAdapter );
        callback.setmLoadingIndicator(comicListLoadingIndicator);
        final Bundle bundleForLoader = null;
        if(comicListLoader == null)
            comicListLoader = (ComicLoader) getSupportLoaderManager().initLoader(comicLoaderId, bundleForLoader, callback);
        comicListLoader.reset();
        comicListLoader.setmLoadingIndicator(comicListLoadingIndicator);
        comicListLoader.forceLoad();
    }
}
