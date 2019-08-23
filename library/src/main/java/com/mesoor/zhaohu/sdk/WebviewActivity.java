package com.mesoor.zhaohu.sdk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class WebviewActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide(); // hide the title bar
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Intent intent = getIntent();
        String token = intent.getStringExtra(DraggableFloatingActionButton.TOKEN);
        WebView webView = findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        String url = Uri.parse("https://www.mesoor.com/").buildUpon().appendQueryParameter("token", token).toString();
        Log.d("url", url);
        webView.loadUrl(url);
    }
}
