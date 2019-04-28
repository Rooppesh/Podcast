package com.android.podcast;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {

    private LayoutInflater inflater;
    Context context;
    ArrayList<EpisodeEntity> episodeEntityArrayList;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    public EpisodeAdapter(Context context, ArrayList<EpisodeEntity> episodeEntityArrayList) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.episodeEntityArrayList = episodeEntityArrayList;
    }

    @NonNull
    @Override
    public EpisodeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.episode_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EpisodeAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.episodeDate.setText(getDate(Long.parseLong(episodeEntityArrayList.get(i).getPub_date_ms()), "dd/MM/yyyy"));
        viewHolder.episodeDescription.setText(episodeEntityArrayList.get(i).getDescription());
        viewHolder.episodeTitle.setText(episodeEntityArrayList.get(i).getTitle());
        int minutes = (Integer.parseInt(episodeEntityArrayList.get(i).getAudio_length()))  / 60;
        int hrs = minutes / 60;
        minutes = minutes%60;

        int seconds = (int)((Integer.parseInt(episodeEntityArrayList.get(i).getAudio_length())) % 60);
        viewHolder.audiolength.setText(hrs + ":" + minutes + ":" + seconds);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            viewHolder.episodeDescription.setText(Html.fromHtml(episodeEntityArrayList.get(i).getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            viewHolder.episodeDescription.setText(Html.fromHtml(episodeEntityArrayList.get(i).getDescription()));
        }
        viewHolder.episodeDescription.setMovementMethod(LinkMovementMethod.getInstance());
//        viewHolder.toggle.setImageResource(R.drawable.expand);
//        viewHolder.toggle.setTag("expand");
        episodeEntityArrayList.get(i).getDescription();

        viewHolder.toggleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cycleTextViewExpansion(viewHolder.episodeDescription);
                if(viewHolder.toggle.getTag().toString().compareTo("expand")==0)
                {
                    viewHolder.toggle.setImageResource(R.drawable.collapse);
                    viewHolder.toggle.setTag("collapse");
                }
                else
                {
                    viewHolder.toggle.setImageResource(R.drawable.expand);
                    viewHolder.toggle.setTag("expand");
                }
            }
        });
        viewHolder.episodeAudioURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(episodeEntityArrayList.get(i).getAudio());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });

        viewHolder.episodeListenNotesURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(episodeEntityArrayList.get(i).getListennotes_url());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });

    }
    private void cycleTextViewExpansion(TextView tv){
        int collapsedMaxLines = 4;
        ObjectAnimator animation = ObjectAnimator.ofInt(tv, "maxLines",
                tv.getMaxLines() == collapsedMaxLines? tv.getLineCount() : collapsedMaxLines);
        animation.setDuration(200).start();
    }

    @Override
    public int getItemCount() {
        return episodeEntityArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView episodeTitle, episodeDescription, episodeDate;
        ImageButton episodeListenNotesURL;
        LinearLayout episodeAudioURL;
        ImageView toggle;
        LinearLayout toggleLayout;
        TextView audiolength;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            episodeTitle = (TextView) itemView.findViewById(R.id.episodeTitle);
            episodeDate = (TextView) itemView.findViewById(R.id.episodeDate);
            episodeDescription = (TextView) itemView.findViewById(R.id.episodeDescription);
            episodeAudioURL = (LinearLayout) itemView.findViewById(R.id.episodeAudioURL);
            episodeListenNotesURL = (ImageButton) itemView.findViewById(R.id.episodeListenNotesURL);
            toggle = (ImageView) itemView.findViewById(R.id.toggle);
            toggleLayout = (LinearLayout) itemView.findViewById(R.id.toggleLayout);
            audiolength = (TextView) itemView.findViewById(R.id.audiolength);
        }
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}