package com.android.podcast;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.content.Context;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenreAdapter  extends RecyclerView.Adapter<GenreAdapter.ViewHolder> implements Filterable {

    private LayoutInflater inflater;
    Context context,activity_context;
    ArrayList<GenreEntity> genreEntityArrayList;
    List<GenreEntity> genreEntityFilteredArrayList;
    int t;

    public GenreAdapter(Context context, ArrayList<GenreEntity> genreEntityArrayList,Context activity_context){
        this.inflater = LayoutInflater.from(context);
        this.context=context;
        this.genreEntityArrayList=genreEntityArrayList;
        this.genreEntityFilteredArrayList=genreEntityArrayList;
        this.activity_context = activity_context;
    }
    @NonNull
    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.genre_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(genreEntityFilteredArrayList.get(i).getName());

        viewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pd = new ProgressDialog(activity_context);
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                // Set the progress dialog title and message
                pd.setMessage("Loading.........");
                // Set the progress dialog background color
                pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));

                pd.setIndeterminate(false);
                 t = 10;
                // Finally, show the progress dialog
                pd.show();
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = "https://listen-api.listennotes.com/api/v2/best_podcasts?genre_id="+genreEntityFilteredArrayList.get(i).getId()+"&page=1&region=us&safe_mode=0";
//                new CountDownTimer(10000, 1000) {
//
//                    @Override
//                    public void onTick(long millisUntilFinished) {
//                        // do something after 1s
//                        t+=10;
//                        pd.setProgress(t);
//
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        // do something end times 5s
//                    }
//
//                }.start();
                StringRequest getRequest = new StringRequest(Request.Method.GET, url,


                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
                                pd.dismiss();
                                Intent intent = new Intent(context, PodcastsActivity.class);
                                intent.putExtra("Podcasts",response);
                                intent.putExtra("Head",genreEntityArrayList.get(i).getName());
                                activity_context.startActivity(intent);
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
    }

    @Override
    public int getItemCount() {
        return genreEntityFilteredArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                Log.d("@@@@@@",charString);
                if (charString.isEmpty()) {
                    genreEntityFilteredArrayList = genreEntityArrayList;
                } else {
                    List<GenreEntity> filteredList = new ArrayList<>();
                    for (GenreEntity row : genreEntityArrayList) {

                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            Log.d("#######",row.getName());

                            filteredList.add(row);
                        }
                    }
                    genreEntityFilteredArrayList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = genreEntityFilteredArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                genreEntityFilteredArrayList = (ArrayList<GenreEntity>) filterResults.values;
                Log.d("#######",genreEntityFilteredArrayList.get(0).getName());

                notifyDataSetChanged();
            }
        };
    }
   public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.GenreName);
        }
    }
}

