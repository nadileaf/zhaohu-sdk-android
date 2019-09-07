package com.mesoor.sdk.example;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.mesoor.zhaohu.sdk.DraggableFloatingActionButton;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
            zhaohu.initialize(this, token, from, this::request);
            runOnUiThread(() -> {
                if (!zhaohu.isShown()) zhaohu.show();
            });
        }, 1, TimeUnit.SECONDS);
    }

    private String request() {
        try {
            URL url = new URL("https://www.mesoor.com");
            URLConnection urlConnection = url.openConnection();
            InputStream in = urlConnection.getInputStream();
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            Log.d("Fake Request", new String(buffer));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fakeUserInfo;
    }

    private String fakeUserInfo = "{\n" +
            "    \"eval\": \"活泼开朗，善于表达，吃苦耐劳，学习能力强，积极向上。\",\n" +
            "    \"basic\": {\n" +
            "        \"name\": \"麦萌\",\n" +
            "        \"email\": \"support@mesoor.com\",\n" +
            "        \"phone\": \"12345678901\",\n" +
            "        \"gender\": \"女\",\n" +
            "        \"birthday\": \"1970-01-01\",\n" +
            "        \"location\": {\n" +
            "            \"city\": \"上海\"\n" +
            "        },\n" +
            "        \"locationId\": 310000\n" +
            "    },\n" +
            "    \"works\": [\n" +
            "        {\n" +
            "            \"end_date\": \"2017-05-19T16:00:00.000Z\",\n" +
            "            \"position\": \"产品运营\",\n" +
            "            \"department\": \"产品运营部\",\n" +
            "            \"industry\": \"1063\",\n" +
            "            \"salary_low\": 4001,\n" +
            "            \"salary_high\": 6000,\n" +
            "            \"until_now\": true,\n" +
            "            \"start_date\": \"2017-02-28T16:00:00.000Z\",\n" +
            "            \"description\": \"\\n· 从测试版上线至今,麦萌一直努力了解每一个职位的岗位职责和具体要求;\\n· 麦萌努力收集每一位主动应聘者,运用统一客观公允的标准筛选每一份简历,帮助企业招聘专员避免遗珠之憾;\\n· 麦萌负责收集整理自有简历库中的沉淀人才,检索潜在适合的候选人;\\n· 麦萌负责为高匹配的候选人协调安排面试,根据候选人的时间安排随时随地进行视频面试;\\n· 麦萌负责采集候选人视频面试表现,分析候选人的职责匹配程度,胜任素质潜力和职业性格,优化企业招聘流程。\\n\",\n" +
            "            \"company\": \"麦穗人工智能\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"skills\": [],\n" +
            "    \"awards\": [],\n" +
            "    \"version\": 1,\n" +
            "    \"projects\": [],\n" +
            "    \"educations\": [\n" +
            "        {\n" +
            "            \"major\": \"教育\",\n" +
            "            \"degree\": \"学术型硕士\",\n" +
            "            \"school\": \"****大学\",\n" +
            "            \"end_date\": \"2016-07-01T00:00:00.000Z\",\n" +
            "            \"start_date\": \"2013-09-01T00:00:00.000Z\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"expectation\": {\n" +
            "        \"locationIds\": [310000],\n" +
            "        \"salary_high\": 6000,\n" +
            "        \"salary_low\": 4001,\n" +
            "        \"exclude\": {\n" +
            "            \"companies\": [],\n" +
            "            \"industries\": [],\n" +
            "            \"work_types\": []\n" +
            "        }\n" +
            "    },\n" +
            "    \"skills_text\": \"不眠不休,兢兢业业\"\n" +
            "}";
}
