package com.example.gos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.gos.databinding.ActivityReportBinding;

public class ReportActivity extends AppCompatActivity {

    private ActivityReportBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
    }

    private void initialize() {
        binding.textView.setText(getIntent().getStringExtra(KEY));
    }

    public static String KEY = "KEY";
}