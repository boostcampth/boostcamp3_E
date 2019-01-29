package com.teame.boostcamp.myapplication.ui;

import android.os.Bundle;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_main);

    }
}
