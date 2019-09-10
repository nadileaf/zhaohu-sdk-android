package com.mesoor.zhaohu.sdk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public abstract class ZhaohuActivity extends AppCompatActivity {

    private WebView webView;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
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
        String from = intent.getStringExtra(DraggableFloatingActionButton.FROM);
        String env = intent.getStringExtra(DraggableFloatingActionButton.ENV);
        this.webView = findViewById(R.id.webview);
        webView.addJavascriptInterface(new JsBridge(), "bridge");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        String url = Uri.parse("https://agora." + env + ".com/")
                .buildUpon()
                .appendQueryParameter("token", token)
                .appendQueryParameter("from", from)
                .appendQueryParameter("platform", "android_sdk")
                .toString();
        Log.d("url", url);
        webView.loadUrl(url);
    }

    protected abstract String requestUserInfo();

    class JsBridge {
        @JavascriptInterface
        public boolean postMessage(String json) {
            try {
                JSONObject message = new JSONObject(json);
                String type = message.getString("type");
                switch (type) {
                    case "USER_INFO_REQUEST":
                        String callback = message.getString("callback");
                        request(callback);
                    case "USER_INFO_REPLY":
                        Log.e("webview bridge", json);
                        return false;
                    case "USER_DENIED":
                        ZhaohuActivity.this.finish();
                        return true;
                    default:
                        return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }

        void request(@NonNull String callback) {
            scheduler.execute(() -> {
                String resume = requestUserInfo();
                try {
                    JSONObject message = new JSONObject(resume);
                    runOnUiThread(() -> {
                        ZhaohuActivity.this.webView.loadUrl("javascript:" + callback + "(" + resume + ")");
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        new AlertDialog.Builder(ZhaohuActivity.this)
                            .setTitle("简历格式错误")
                            .setMessage(e.getMessage())
                            .show();
                    });
                }
            });
        }
    }
}

