package com.zflabs.marveldatabase;

import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zflabs.marveldatabase.adapter.CharacterAdapter;
import com.zflabs.marveldatabase.data.Character;
import com.zflabs.marveldatabase.loader.CharacterLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Character>>,
        CharacterAdapter.MarvelAdapterClickHandler{

    private static final int CHARACTER_LIST_LOADER_ID = 213;
    private static final String TAG = "signin1";
    private static final int STATE_SIGNED_IN = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private CharacterAdapter characterAdapter;
    private static final int RC_SIGN_IN = 0;

    private static final String CHARACTERS_LIST_STATE="charactersListState";

    @BindView(R.id.characters_list)
    RecyclerView charactersList;

    @BindView(R.id.character_starts_with)
    TextView characterStartsWith;

    @BindView(R.id.characters_list_load_message)
    TextView mErrorMessageDisplay;

    @BindView(R.id.characters_list_load_image)
    ProgressBar mLoadingIndicator;

    @BindView(R.id.my_toolbar)
    Toolbar myToolbar;

    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;
    private CharacterLoader listLoader;

    private TextView mStatusTextView;

    private int mSignInProgress;
    private PendingIntent mSignInIntent;
    private int mSignInError;
    private Button mSignOutButton;
    private Button mRevokeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        charactersList.setLayoutManager(new GridLayoutManager(this, 2));
        if(savedInstanceState!=null && savedInstanceState.getParcelable(CHARACTERS_LIST_STATE)!=null){
            charactersList.setVisibility(View.VISIBLE);
            Parcelable charactersListState = savedInstanceState.getParcelable(CHARACTERS_LIST_STATE);
            charactersList.getLayoutManager().onRestoreInstanceState(charactersListState);
            performSearch(characterStartsWith);
        }
        startBrowsing();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable parcelable = charactersList.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(CHARACTERS_LIST_STATE, parcelable);
        CharacterAdapter adapter = (CharacterAdapter) charactersList.getAdapter();
    }

    private void startBrowsing() {

        characterAdapter = new CharacterAdapter(this);
        charactersList.setAdapter(characterAdapter);

        characterStartsWith.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(v);
                    return true;
                }
                return false;
            }
        });
    }

    private void performSearch(TextView v) {
        String searchText = v.getText().toString();
        Log.i(TAG, "Setting screen name: " + searchText);
        charactersList.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        final int loaderId = CHARACTER_LIST_LOADER_ID;
        final LoaderManager.LoaderCallbacks<List<Character>> callback = MainActivity.this;
        final Bundle bundleForLoader = null;
        if(listLoader == null) {
            listLoader = (CharacterLoader) getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);
        }
        listLoader.searchParam = searchText;
        listLoader.reset();
        listLoader.forceLoad();
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            getSupportLoaderManager().restartLoader(CHARACTER_LIST_LOADER_ID, null, this);
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }

    @Override
    public Loader<List<Character>> onCreateLoader(int id, final Bundle loaderArgs) {
        return new CharacterLoader(this, characterStartsWith.getText().toString(), mLoadingIndicator);
    }

    @Override
    public void onLoadFinished(Loader<List<Character>> loader, List<Character> data) {
        characterAdapter.setCharacterData(data);
        charactersList.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.GONE);
        if (null == data || data.size()==0) {
            showErrorMessage("No result");
        } else {
            showResult();
        }
    }

    private void showErrorMessage(String message) {
        charactersList.setVisibility(View.GONE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setText(message);
    }

    private void showResult() {
        mErrorMessageDisplay.setVisibility(View.GONE);
        charactersList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<Character>> loader) {
        loader.cancelLoad();
        loader.reset();
        loader.forceLoad();
    }

    @Override
    public void onCharacterClick(Character character) {
        int dataToSend = character.getId();
        String name = character.getName();
        Log.i(TAG, "Setting screen name: " + name);
        Context context = this;
        Class destinationClass = CharacterActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);

        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, dataToSend);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intentToStartDetailActivity, bundle);
        } else {
            startActivity(intentToStartDetailActivity);
        }

       // startActivity(intentToStartDetailActivity);
    }
}
