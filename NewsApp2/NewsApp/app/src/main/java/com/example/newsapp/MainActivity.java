package com.example.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.LoaderManager;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String GUARDIANS_REQUEST_URL="https://content.guardianapis.com/search?api-key=920c6424-4ae9-4854-bbf7-84297d8e7f9b&show-tags=contributor";
    private static final int NEWS_LOADER_ID = 1;
    private NewsAdapter mNewsAdapter;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView newsListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_state);
        mNewsAdapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setEmptyView(mEmptyStateTextView);
        newsListView.setAdapter(mNewsAdapter);


        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                News currentNew = mNewsAdapter.getItem(position);
                Uri newUri = Uri.parse(currentNew.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newUri);
                startActivity(websiteIntent);
            }
        });
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
    if (networkInfo != null && networkInfo.isConnected()){
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(NEWS_LOADER_ID,null,this);
    } else {
        mEmptyStateTextView.setText(R.string.no_internet_connection);
    }

    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy  = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        Uri baseUri = Uri.parse(GUARDIANS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("orderBy", orderBy);
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {
        mEmptyStateTextView.setText(R.string.no_news);
        mNewsAdapter.clear();
        if (data != null && !data.isEmpty()) {
            mNewsAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
mNewsAdapter.clear();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            Log.v(LOG_TAG, "Starting intent");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}