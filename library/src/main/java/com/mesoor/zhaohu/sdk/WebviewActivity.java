package com.mesoor.zhaohu.sdk;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;

public class WebviewActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        if (getSupportActionBar() != null){
            getSupportActionBar().hide(); // hide the title bar
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView webView = findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        webView.loadUrl("https://www.mesoor.com/");
    }
}
