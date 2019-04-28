package com.android.podcast;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.ViewHolder>{

    private LayoutInflater inflater;

    Context context,ctx;
    ArrayList<PodcastEntity> podcastEntityArrayList;
    private RequestOptions requestOptions;
    RequestManager glide;


    public PodcastAdapter(Context context, ArrayList<PodcastEntity> podcastEntityArrayList, Context ctx, RequestManager glide) {
        this.inflater= LayoutInflater.from(context);
        this.context = context;
        this.ctx = ctx;
        this.podcastEntityArrayList = podcastEntityArrayList;
        this.glide = glide;
        requestOptions  = new RequestOptions();
    }

    @NonNull
    @Override
    public PodcastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.podcast_item, viewGroup, false);
        return new ViewHolder(view);
    }
    private CircularProgressDrawable getCircularDrawable() {
        final CircularProgressDrawable circularProgressDrawable;
        circularProgressDrawable = new CircularProgressDrawable(ctx);
        circularProgressDrawable.setColorFilter(ContextCompat.getColor(ctx, R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }
    @Override
    public void onBindViewHolder(@NonNull PodcastAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.title.setText(podcastEntityArrayList.get(i).getTitle());
        viewHolder.publisher.setText(podcastEntityArrayList.get(i).getPublisher());
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = new ProgressDialog(ctx);
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                // Set the progress dialog title and message
                pd.setMessage("Loading.........");
                // Set the progress dialog background color
                pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));

                pd.setIndeterminate(false);
                // Finally, show the progress dialog
                pd.show();
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = "https://listen-api.listennotes.com/api/v2/podcasts/"+podcastEntityArrayList.get(i).getId();
                StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("####", response);

                                Intent intent = new Intent(context, DetailsActivity.class);
                                intent.putExtra("Details",response);
                                pd.dismiss();
                                ctx.startActivity(intent);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("ERROR","error => "+error.toString());
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("X-ListenAPI-Key", "5ab10558f20e43b08945431dbfa6e8f1");

                        return params;
                    }
                };
                queue.add(getRequest);
            }
        });
        final CircularProgressDrawable circularProgressDrawable = getCircularDrawable();

        requestOptions.placeholder(circularProgressDrawable);
        requestOptions.error(R.drawable.icon);

        glide.applyDefaultRequestOptions(requestOptions).load(podcastEntityArrayList.get(i).getTumbnail()).listener(new  RequestListener<Drawable>() {
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


        }).into(viewHolder.img);


    }

    @Override
    public int getItemCount() {
        return podcastEntityArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView publisher;
        ImageView img;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.podcastTitle);
            publisher = (TextView) itemView.findViewById(R.id.podcastPublisher);
            img = (ImageView) itemView.findViewById(R.id.podcastImg);
            layout = (LinearLayout) itemView.findViewById(R.id.podcastLayout);
        }
    }
}
