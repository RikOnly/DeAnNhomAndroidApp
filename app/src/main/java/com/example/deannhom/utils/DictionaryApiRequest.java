package com.example.deannhom.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class DictionaryApiRequest {

    private static final String API_ENDPOINT = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    public interface DefinitionCallback {
        void onDefinitionFetched(String definition);
    }

    public void fetchDefinitionFromApi(String word, DefinitionCallback callback) {
        try {
            String encodedWord = URLEncoder.encode(word, "UTF-8");
            URL url = new URL(API_ENDPOINT + encodedWord);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                String responseData = stringBuilder.toString();

                // Extract definition from the response data
                // TODO: Get all definition of all words.
                JSONArray jsonArray = new JSONArray(responseData);

                callback.onDefinitionFetched(jsonArray.getJSONObject(0).toString());
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                callback.onDefinitionFetched(null);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}