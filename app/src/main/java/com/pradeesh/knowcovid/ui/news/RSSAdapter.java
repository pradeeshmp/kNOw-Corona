package com.pradeesh.knowcovid.ui.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.pradeesh.knowcovid.ui.RSSDetailedWebActivity;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.pradeesh.knowcovid.utils.Constant.RSS_HEADLINEKEY;
import static com.pradeesh.knowcovid.utils.Constant.RSS_URLKEY;

public class RSSAdapter extends RecyclerView.Adapter<RSSAdapter.RSSViewHolder>{

    private static final String LOG_TAG = RSSAdapter.class.getSimpleName();
    private List<Article> articles;
    private Context context;
    private PrettyTime prettyTime;

    public RSSAdapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
        prettyTime = new PrettyTime();
    }

    @NonNull
    @Override
    public RSSViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rss_item, parent,false);
        return new RSSViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RSSViewHolder holder, final int position) {
        holder.title.setText(articles.get(position).getTitle());
        holder.date.setText(prettyDateFormattter(articles.get(position).getPublishedAt()));


        String photoUrl = articles.get(position).getUrlToImage();

        if(photoUrl != null){
            rssImageResourceUpdate(holder.imageView, photoUrl);
        }
        //Glide.with(context).load(photoUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }


    public class RSSViewHolder extends RecyclerView.ViewHolder{
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
                    Intent i = new Intent(context, RSSDetailedWebActivity.class);
                    i.putExtra(RSS_URLKEY,articles.get(position).getUrl());
                    i.putExtra(RSS_HEADLINEKEY,articles.get(position).getTitle());
                    context.startActivity(i);
                }
            });
        }
    }

    private void rssImageResourceUpdate(ImageView imageView, String url){
        Glide.with(context)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e(LOG_TAG, "Resoruce Loading failed : " + url);
                        imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_launcher, null));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.e(LOG_TAG, "Resoruce ready");
                        return false;
                    }
                })
                .into(imageView);
    }


    private String prettyDateFormattter(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch ( ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return prettyTime.format(convertedDate);
    }

}
