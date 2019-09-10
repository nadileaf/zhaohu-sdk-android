package com.mesoor.sdk.example;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.mesoor.zhaohu.sdk.DraggableFloatingActionButton;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final ScheduledExecutorService schedulerExecutor = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DraggableFloatingActionButton zhaohu = findViewById(R.id.zhaohu);
        zhaohu.hide();

        // 模拟网络延迟
        schedulerExecutor.schedule(() -> {
            String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjUxNzYiLCJmcm9tIjoidGVzdCIsImlhdCI6MTU2MDgyNTI3NywiZXhwIjoxNjIzODk3MjY1fQ.fw77bPa-Bh3sqW9YpopwEVRXIXByioSxh-elUXca4JI";
            String from = "test";
            zhaohu.initialize(this, token, from, ZhaohuActivity.class);
            runOnUiThread(() -> {
                if (!zhaohu.isShown()) zhaohu.show();
            });
        }, 1, TimeUnit.SECONDS);
    }

}
