package com.pradeesh.knowcovid.ui.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pradeesh.knowcovid.model.Article;
import com.pradeesh.knowcovid.R;
import com.pradeesh.knowcovid.model.VehicleEventMessage;
import com.pradeesh.knowcovid.ui.RSSDetailedWebActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.pradeesh.knowcovid.utils.Constant.RSS_HEADLINEKEY;
import static com.pradeesh.knowcovid.utils.Constant.RSS_URLKEY;
import static com.pradeesh.knowcovid.utils.Constant.VEH_IGNITION_STATUS_ON;
import static com.pradeesh.knowcovid.utils.Constant.VEH_SPEED_LIMIT;

public class RSSAdapter extends RecyclerView.Adapter<RSSAdapter.RSSViewHolder> {

    private TextToSpeech textToSpeech;
    private static final String LOG_TAG = RSSAdapter.class.getSimpleName();
    private List<Article> articles;
    private Context context;
    private PrettyTime prettyTime;

    private int currentSpeed;
    private String currentIgnitionStatus;

    public RSSAdapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
        prettyTime = new PrettyTime();
        initTTS();
        EventBus.getDefault().register(this);
    }

    @NonNull
    @Override
    public RSSViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rss_item, parent, false);
        return new RSSViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RSSViewHolder holder, final int position) {
        holder.title.setText(articles.get(position).getTitle());
        holder.date.setText(prettyDateFormatter(articles.get(position).getPublishedAt()));


        String photoUrl = articles.get(position).getUrlToImage();

        if (photoUrl != null) {
            rssImageResourceUpdate(holder.imageView, photoUrl);
        }
    }


    @Override
    public int getItemCount() {
        return articles.size();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        EventBus.getDefault().unregister(this);
    }

    /**
     *So many helper funcitons in this Adapter - migrate to helper classes - v2
     */

    /**
     * Event bus subscription
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(VehicleEventMessage event) {
        /* Do something */
        Log.v(LOG_TAG, event.getSpeedValue() + "");
        Log.v(LOG_TAG, event.getIgnitionStatus());
        currentSpeed = event.getSpeedValue();
        currentIgnitionStatus = event.getIgnitionStatus();
    }

    public class RSSViewHolder extends RecyclerView.ViewHolder {
        TextView title, date;
        ImageView imageView;

        public RSSViewHolder(@NonNull final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            imageView = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    enableDetailedRSSView(position);
                    handleRSSBasedOnVehicleStatus(position);
                }
            });
        }
    }

    private void enableDetailedRSSView(int position){
        Intent i = new Intent(context, RSSDetailedWebActivity.class);
        i.putExtra(RSS_URLKEY, articles.get(position).getUrl());
        i.putExtra(RSS_HEADLINEKEY, articles.get(position).getTitle());
        context.startActivity(i);
    }

    private void handleRSSBasedOnVehicleStatus(int position){
        if(currentSpeed > VEH_SPEED_LIMIT && currentIgnitionStatus.equals(VEH_IGNITION_STATUS_ON)){
            textToSpeech.speak(articles.get(position).getTitle(), TextToSpeech.QUEUE_FLUSH, null, null);
            Toast.makeText(context, "kNOw Corona : Application in Vehicle ON mode!", Toast.LENGTH_LONG).show();
        }
    }

    private void rssImageResourceUpdate(ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.v(LOG_TAG, "Resoruce Loading failed : " + url);
                        imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_launcher, null));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.v(LOG_TAG, "Resoruce ready");
                        return false;
                    }
                })
                .into(imageView);
    }





    private String prettyDateFormatter(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return prettyTime.format(convertedDate);
    }

    private void initTTS() {
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.v(LOG_TAG, "TTS : The Language is not supported!");
                    } else {
                        Log.v(LOG_TAG, "TTS : Language Supported.");
                    }
                    Log.v(LOG_TAG, "TTS : Initialization success.");
                } else {
                    Log.v(LOG_TAG, "TTS : Initialization failed.");
                }
            }
        });

    }


}
