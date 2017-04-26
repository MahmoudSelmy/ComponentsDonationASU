package com.sheatouk.selmy.componentsdonationasu.Activity;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sheatouk.selmy.componentsdonationasu.R;
import com.sheatouk.selmy.componentsdonationasu.Services.ScrapCompService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // permit strictMode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        startService(new Intent(this , ScrapCompService.class));
    }
}
