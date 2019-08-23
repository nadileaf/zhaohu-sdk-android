package com.mesoor.sdk.example;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.mesoor.zhaohu.sdk.DraggableFloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DraggableFloatingActionButton zhaohu = findViewById(R.id.zhaohu);
        zhaohu.initialize(this, "???");
    }

}
