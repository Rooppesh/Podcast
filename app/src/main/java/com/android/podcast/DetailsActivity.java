package com.android.podcast;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.content.Context;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    Context context;
    String details;
    TextView detailTitle,json,detailPublisher,detailDescription;
    ImageView detailImg;
    JSONObject obj;
    RequestOptions requestOptions;
    RequestManager glide;
    RecyclerView recyclerView;
    EpisodeAdapter episodeAdapter;
    LinearLayoutManager linearLayoutManager;
    ImageButton explicit;
    ImageButton email, website, listennotes;
    TextView country, language, next_episode;
    ImageView toggle;
    ScrollView ss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        context = this;
        Intent intent = getIntent();
        details = intent.getStringExtra("Details");
        try {
            obj = new JSONObject(details);
            recyclerView = findViewById(R.id.detailEpisode);
//          recyclerView.setNestedScrollingEnabled(false);
            linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.addItemDecoration(new ItemDivider(this));
            recyclerView.setLayoutManager(linearLayoutManager);
            JSONArray arr = obj.getJSONArray("episodes");
            ArrayList<EpisodeEntity> episodeEntityArrayList = new ArrayList<EpisodeEntity>();
            EpisodeEntity episodeEntity;
            String title;
            String description;
            String audio;
            String listennotes_url;
            String pub_date_ms;
            String thumbnail;
            String image;
            String audio_length;
            for (int i = 0; i < arr.length(); i++) {
                title = arr.getJSONObject(i).getString("title");
                description = arr.getJSONObject(i).getString("description");
                audio = arr.getJSONObject(i).getString("audio");
                listennotes_url = arr.getJSONObject(i).getString("listennotes_url");
                pub_date_ms = arr.getJSONObject(i).getString("pub_date_ms");
                thumbnail = arr.getJSONObject(i).getString("thumbnail");
                image = arr.getJSONObject(i).getString("image");
                audio_length = arr.getJSONObject(i).getString("audio_length");
                episodeEntity = new EpisodeEntity(title, description, audio, listennotes_url, pub_date_ms, thumbnail, image, audio_length);
                episodeEntityArrayList.add(episodeEntity);
                Log.d("#####",title);
            }
            episodeAdapter= new EpisodeAdapter(context,episodeEntityArrayList);
            recyclerView.setAdapter(episodeAdapter);
            Log.d("#####", String.valueOf(episodeEntityArrayList.size()));
//            json = findViewById(R.id.json);
//            json.setText(String.valueOf(obj));
            //json.setVisibility(View.GONE);







            detailTitle = findViewById(R.id.detailTitle);
            detailTitle.setText(obj.get("title").toString());
            detailImg = findViewById(R.id.detailImg);
            explicit = findViewById(R.id.explicit);
            email = findViewById(R.id.email);
            website = findViewById(R.id.website);
            listennotes = findViewById(R.id.listennotes);
            glide = Glide.with(context);
            final CircularProgressDrawable circularProgressDrawable = getCircularDrawable();
            requestOptions  = new RequestOptions();
            requestOptions.placeholder(circularProgressDrawable);
            requestOptions.error(R.drawable.icon);
            glide.applyDefaultRequestOptions(requestOptions).load(obj.get("thumbnail")).listener(new  RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    circularProgressDrawable.stop();
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    circularProgressDrawable.stop();
                    return false;
                }


            }).into(detailImg);
detailImg.setFocusable(true);
      try{  JSONArray genreArray = obj.getJSONArray("genres");
            String genreName;
            ArrayList<String> genreNameList= new ArrayList<String>();
            for (int i = 0; i < genreArray.length(); i++) {
                genreName = (String) genreArray.get(i);
                genreNameList.add(genreName);
                Log.d("#####",genreName);
            }

      }
      catch (JSONException e) {
          e.printStackTrace();
      }



            explicit = findViewById(R.id.explicit);
            if(obj.get("explicit_content").toString().compareTo("false")==0)
                explicit.setVisibility(View.GONE);

            email = findViewById(R.id.email);
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    try {
                        intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto",obj.get("email").toString(), null));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra(Intent.EXTRA_SUBJECT, "");
                    intent.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                }
            });
            website  = findViewById(R.id.website);
            website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = null;
                    try {
                        uri = Uri.parse(obj.get("website").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            });
            listennotes = findViewById(R.id.listennotes);
            listennotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = null;
                    try {
                        uri = Uri.parse(obj.get("listennotes_url").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            });


            country = findViewById(R.id.country);
            country.setText(obj.get("country").toString());

            language = findViewById(R.id.language);
            language.setText(obj.get("language").toString());



            detailPublisher = findViewById(R.id.detailPublisher);
            detailPublisher.setText(obj.get("publisher").toString());

            detailDescription = findViewById(R.id.detailDescription);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                detailDescription.setText(Html.fromHtml(obj.get("description").toString(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                detailDescription.setText(Html.fromHtml(obj.get("description").toString()));
            }

            toggle = (ImageView) findViewById(R.id.toggle);

            toggle.setImageResource(R.drawable.expand);
            toggle.setTag("expand");

            toggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cycleTextViewExpansion(detailDescription);
                    if(toggle.getTag().toString().compareTo("expand")==0)
                    {
                        toggle.setImageResource(R.drawable.collapse);
                        toggle.setTag("collapse");
                    }
                    else
                    {
                        toggle.setImageResource(R.drawable.expand);
                        toggle.setTag("expand");
                    }
                }
            });

            Log.d("@@@", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private CircularProgressDrawable getCircularDrawable() {
        final CircularProgressDrawable circularProgressDrawable;
        circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }
    private void cycleTextViewExpansion(TextView tv){
        int collapsedMaxLines = 3;
        ObjectAnimator animation = ObjectAnimator.ofInt(tv, "maxLines",
                tv.getMaxLines() == collapsedMaxLines? tv.getLineCount() : collapsedMaxLines);
        animation.setDuration(200).start();
    }

}
