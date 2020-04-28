package com.pradeesh.knowcovid.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pradeesh.knowcovid.R;

import static com.pradeesh.knowcovid.utils.Constant.RSS_HEADLINEKEY;
import static com.pradeesh.knowcovid.utils.Constant.RSS_URLKEY;

public class RSSDetailedWebActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String rssURLToLoad;
    private String rssHeadline;

    private WebView rssFeedWebView;
    private ProgressBar rssDetailedprogressBar;
    private TextView textView_loading;

    private WebSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rssdetailed);
        rssURLToLoad = getIntent().getExtras().getString(RSS_URLKEY);
        rssHeadline = getIntent().getExtras().getString(RSS_HEADLINEKEY);

        initUI();
        initWebSettings();


        rssFeedWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                rssDetailedprogressBar.setVisibility(View.GONE);
                textView_loading.setVisibility(View.GONE);
                rssFeedWebView.setVisibility(View.VISIBLE);
            }
        });
        rssFeedWebView.loadUrl(rssURLToLoad);
        backPress();
    }

    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        rssFeedWebView = findViewById(R.id.rssDetailedwebView);
        rssDetailedprogressBar = findViewById(R.id.rssDetailedprogressBar);
        textView_loading = findViewById(R.id.textView_rss_load);

        toolbar.setTitle(rssHeadline);
    }

    private void initWebSettings() {
        settings = rssFeedWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
    }

    private void backPress() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
