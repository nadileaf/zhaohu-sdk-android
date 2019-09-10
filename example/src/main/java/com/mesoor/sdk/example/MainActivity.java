package com.mesoor.sdk.example;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.mesoor.zhaohu.sdk.DraggableFloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        EditText tokenEditText = findViewById(R.id.tokenEditText);
        EditText fromEditText = findViewById(R.id.fromEditText);
        RadioGroup envRadioGroup = findViewById(R.id.envRadioGroup);
        Button initializeZhaohuButton = findViewById(R.id.initializeZhaohuButton);

        DraggableFloatingActionButton zhaohu = findViewById(R.id.zhaohu);
        zhaohu.hide();

        initializeZhaohuButton.setOnClickListener(view -> {
            zhaohu.initialize(
                    this,
                    tokenEditText.getText().toString(),
                    fromEditText.getText().toString(),
                    envRadioGroup.getCheckedRadioButtonId() == R.id.mesoorRadioButton ? "mesoor" : "nadileaf",
                    ZhaohuActivity.class
            );
            if (!zhaohu.isShown()) zhaohu.show();
        });
        setSupportActionBar(toolbar);
    }

}
