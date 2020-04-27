package com.pradeesh.knowcovid.ui.map;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pradeesh.knowcovid.R;
import com.pradeesh.knowcovid.utils.Constant;

import static com.pradeesh.knowcovid.utils.Constant.MAPURL;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements HandleBackPress {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private WebView covidWebView;
    private ProgressBar progressBar;
    private TextView loadingTxt;

    private WebSettings settings;

    private Bundle webViewBundle;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        initUI(rootView);
        initWebSettings();

        covidWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                loadingTxt.setVisibility(View.GONE);
                covidWebView.setVisibility(View.VISIBLE);
            }
        });

        if (webViewBundle != null) {
            covidWebView.restoreState(webViewBundle);
        } else {
            covidWebView.loadUrl(MAPURL);
        }
        return rootView;
    }

    private void initUI(View rootView) {
        covidWebView = rootView.findViewById(R.id.webview);
        progressBar = rootView.findViewById(R.id.progressBar);
        loadingTxt = rootView.findViewById(R.id.textView_load);
    }

    private void initWebSettings() {
        settings = covidWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        // Enable AppCache
        settings.setAppCacheEnabled(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        webViewBundle = new Bundle();
        covidWebView.saveState(webViewBundle);
    }

    @Override
    public boolean onBackPressed() {
        if (covidWebView.canGoBack()) {
            covidWebView.goBack();
            return true;
        } else {
            return false;
        }
    }


}
