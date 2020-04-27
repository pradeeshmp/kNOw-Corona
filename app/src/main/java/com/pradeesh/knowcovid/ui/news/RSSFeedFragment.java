package com.pradeesh.knowcovid.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pradeesh.knowcovid.model.Article;
import com.pradeesh.knowcovid.R;

import java.util.List;

public class RSSFeedFragment extends Fragment {

    RSSViewModel rssViewModel;
    RecyclerView recyclerView;

    private ProgressBar rssProgressBar;
    private TextView textView_rssLoading;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rssViewModel = new ViewModelProvider(this).get(RSSViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rss, container, false);


        initRSSView(root);

        rssViewModel.getAllArticle().observe(getViewLifecycleOwner(), new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(new RSSAdapter(articles, getContext()));
                if (articles.size() > 0) {
                    rssProgressBar.setVisibility(View.GONE);
                    textView_rssLoading.setVisibility(View.GONE);
                }
            }
        });

        return root;
    }

    private void initRSSView(View root) {
        recyclerView = root.findViewById(R.id.rssRecyclerView);
        rssProgressBar = root.findViewById(R.id.rssProgressBar);
        textView_rssLoading = root.findViewById(R.id.textView_rssLoad);
    }
}
