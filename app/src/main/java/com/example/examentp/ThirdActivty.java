package com.example.examentp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ThirdActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_activty);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}