package com.example.android.haqdarshak_akshay_android_test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    private RecyclerView rv;
    Handler mUserInteractionHandler;
    Runnable logsout;
    long LOGOUT_TIME = 600000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = (RecyclerView) findViewById(R.id.recyclerView);
        mUserInteractionHandler = new Handler();
        getPosts();
        logsout = new Runnable() {
            @Override
            public void run() {
                finish();
            }
        };
        mUserInteractionHandler.postDelayed(logsout, LOGOUT_TIME);
    }

    private void getPosts(){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(), "/me/feed", null, HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        ArrayList<ArrayList<String>> feedList = extractData(response);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                        rv.setLayoutManager(layoutManager);
                        rv.setHasFixedSize(true);
                        rv.setAdapter(new customAdapter(feedList));
                    }
                }
        ).executeAsync();
    }

    private ArrayList<ArrayList<String>> extractData(GraphResponse response) {
        ArrayList<ArrayList<String>> storyList = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(response.getRawResponse());
            JSONArray feedArray = jsonResponse.getJSONArray("data");
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feed = feedArray.getJSONObject(i);
                String story = feed.getString("story");
                String date = feed.getString("created_time").substring(0,10);
                ArrayList<String> individualStory = new ArrayList<>();
                individualStory.add(story);
                individualStory.add(date);
                storyList.add(individualStory);
            }
        } catch (JSONException e) {
            Log.e("NetworkUtils", "Problem parsing JSON results", e);
        }
        return storyList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logout();
    }

    private void logout(){
        LoginManager.getInstance().logOut();
        Intent login = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(login);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to logout", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    @Override
    public void onUserInteraction(){
        mUserInteractionHandler.removeCallbacks(logsout);
        mUserInteractionHandler.postDelayed(logsout, LOGOUT_TIME);
    }
}
