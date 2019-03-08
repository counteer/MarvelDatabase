package com.zflabs.marveldatabase.util;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zflabs.marveldatabase.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_URL =
            "https://gateway.marvel.com:443/v1/public/";
    private static final String CHARACTERS_PATH = "characters";
    private static final String COMICS_PATH = "comics";
    private static final String API_KEY_PARAM = "apikey";
    private static final String LIMIT_PARAM = "limit";
    private static final String TS_PARAM = "ts";
    private static final String ORDER_BY = "orderBy";
    private static final String FOC_DATE = "-focDate";
    private static final String HASH_PARAM = "hash";
    private static final String NAME_STARTS_WITH_PARAM = "nameStartsWith";
    private static final String API_KEY = BuildConfig.publicKey;

    private static String generateHash(String random, String publicKey){
        String hash = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] bytesOfMessage = (random + BuildConfig.privateKey + publicKey).getBytes("UTF-8");
            byte[] digest = md5.digest(bytesOfMessage);
            BigInteger number = new BigInteger(1, digest);
            hash = number.toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return hash;
    }

    public static URL buildUrl(String startsWith) {

        String randomText = Long.toString(new Date().getTime());
        String hash = generateHash(randomText, API_KEY);
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(CHARACTERS_PATH)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LIMIT_PARAM, "100")
                .appendQueryParameter(TS_PARAM, randomText)
                .appendQueryParameter(HASH_PARAM, hash)
                .appendQueryParameter(NAME_STARTS_WITH_PARAM, startsWith)
                .build();

        URL url = convertUriToURL(builtUri);

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildUrlForComics(int characterId) {
        String randomText = Long.toString(new Date().getTime());
        String hash = generateHash(randomText, API_KEY);
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(CHARACTERS_PATH)
                .appendPath(String.valueOf(characterId))
                .appendPath(COMICS_PATH)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LIMIT_PARAM, "18")
                .appendQueryParameter(TS_PARAM, randomText)
                .appendQueryParameter(HASH_PARAM, hash)
                .appendQueryParameter(ORDER_BY, FOC_DATE)
                .build();
        URL url = convertUriToURL(builtUri);

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildUrl(int characterId) {

        String randomText = Long.toString(new Date().getTime());
        String hash = generateHash(randomText, API_KEY);
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(CHARACTERS_PATH)
                .appendPath(characterId+"")
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(TS_PARAM, randomText)
                .appendQueryParameter(HASH_PARAM, hash)
                .build();
        URL url = convertUriToURL(builtUri);
        Log.v(TAG, "Built URI " + url);
        return url;
    }

    @Nullable
    private static URL convertUriToURL(Uri builtUri) {
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(5000);
        try (InputStream in = urlConnection.getInputStream();
             Scanner scanner = new Scanner(in);) {
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}