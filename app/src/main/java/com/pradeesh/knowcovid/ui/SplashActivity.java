package com.pradeesh.knowcovid.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.TextView;

import com.pradeesh.knowcovid.MainActivity;
import com.pradeesh.knowcovid.R;
import com.pradeesh.knowcovid.utils.Constant;

import static com.pradeesh.knowcovid.utils.Constant.SPLASH_APPNAME;

public class SplashActivity extends AppCompatActivity {

    private TextView textView_appTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textView_appTitle = findViewById(R.id.titleText);
        textView_appTitle.setText(Html.fromHtml(SPLASH_APPNAME, 0));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, 3000);
    }
}
