package com.example.newsapp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<News> fetchNewsData(String requestUrl, Context context) {
        URL url = createUrl(requestUrl);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<News> news = extractFeatureFromJson(jsonResponse, context);
        return news;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractFeatureFromJson(String newsJSON, Context context) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        List<News> news = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(newsJSON);
            if (root.has(context.getString(R.string.response_object))) {
                JSONObject responseJSONObject = root.getJSONObject(context.getString(R.string.response_object));
                if (responseJSONObject.has(context.getString(R.string.results_array))) {
                    JSONArray resultsJSONArray = responseJSONObject.getJSONArray(context.getString(R.string.results_array));
                    for (int i = 0; i < resultsJSONArray.length(); i++) {
                        JSONObject currentJSONObject = resultsJSONArray.getJSONObject(i);
                        String title;
                        if (currentJSONObject.has(context.getString(R.string.title))) {
                            title = currentJSONObject.getString(context.getString(R.string.title));
                        } else {
                            title = null;
                        }
                        String section;
                        if (currentJSONObject.has(context.getString(R.string.section_name))) {
                            section = currentJSONObject.getString(context.getString(R.string.section_name));
                        } else {
                            section = null;
                        }
                        String dateAndTime;
                        if (currentJSONObject.has(context.getString(R.string.date_and_time))) {
                            dateAndTime = currentJSONObject.getString(context.getString(R.string.date_and_time));
                        } else {
                            dateAndTime = null;
                        }
                        String webUrl;
                        if (currentJSONObject.has(context.getString(R.string.web_url))) {
                            webUrl = currentJSONObject.getString(context.getString(R.string.web_url));
                        } else {
                            webUrl = null;
                        }
                        ArrayList<String> authors = new ArrayList<>();
                        if (currentJSONObject.has(context.getString(R.string.tags_array))) {
                            JSONArray currentTagsArray = currentJSONObject.getJSONArray(context.getString(R.string.tags_array));
                            if (currentTagsArray == null || currentTagsArray.length() == 0) {
                                authors = null;
                            } else {
                                for (int j = 0; j < currentTagsArray.length(); j++) {
                                    JSONObject currentObjectInTags = currentTagsArray.getJSONObject(j);
                                    authors.add(currentObjectInTags.getString(context.getString(R.string.web_title_in_tags_array)));
                                }
                            }
                        } else {
                            authors = null;
                        }

                        News newsArrayList = new News(title, dateAndTime, section, webUrl, authors);
                        news.add(newsArrayList);

                    }

                }

            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return news;
    }
}



