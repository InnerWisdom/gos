package com.example.gos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    private void initialize() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, TaskListFragment.newInstance(getApplication()))
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit();
    }
}