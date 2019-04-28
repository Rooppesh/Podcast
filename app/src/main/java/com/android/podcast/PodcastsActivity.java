package com.android.podcast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.content.Context;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PodcastsActivity extends AppCompatActivity {
    String message,head;
    PodcastAdapter podcastAdapter;
    TextView header;
    LinearLayoutManager linearLayoutManager;
    Context context,activity_context;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast);
        Intent intent = getIntent();
        message = intent.getStringExtra("Podcasts");
        head = intent.getStringExtra("Head");
        header = (TextView) findViewById(R.id.podcastHeader);
        header.setText(head);
        recyclerView = (RecyclerView) findViewById(R.id.PodcastRecyclerView);
        context = getApplicationContext();
        linearLayoutManager = new LinearLayoutManager(context);
        activity_context = this;
        Log.d("#####",message);
        LoadPoadcasts();

    }
    public void LoadPoadcasts()
    {
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new ItemDivider(this));
        try {
            JSONObject obj = new JSONObject(message);
            JSONArray arr = obj.getJSONArray("channels");
            ArrayList<PodcastEntity> podcastEntityArrayList = new ArrayList<PodcastEntity>();
            PodcastEntity podcastEntity;
            String id;
            String publisher;
            String title;
            String tumbnail;
            for (int i = 0; i < arr.length(); i++) {
                id = arr.getJSONObject(i).getString("id");
                publisher = arr.getJSONObject(i).getString("publisher");
                title = arr.getJSONObject(i).getString("title");
                tumbnail = arr.getJSONObject(i).getString("thumbnail");
                podcastEntity = new PodcastEntity(id,publisher,title,tumbnail);
                podcastEntityArrayList.add(podcastEntity);
                Log.d("#####",id+"  "+publisher+"  "+title+"  "+tumbnail);
            }
            podcastAdapter= new PodcastAdapter(context,podcastEntityArrayList, activity_context,Glide.with(activity_context));
            recyclerView.setAdapter(podcastAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
