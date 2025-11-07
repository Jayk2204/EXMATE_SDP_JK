package com.example.exmate_sdp.views.activites;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;

import com.example.exmate_sdp.views.fragments.AddTransactionFragment;
import com.example.exmate_sdp.R;
import com.example.exmate_sdp.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("Transactions");
        calendar = Calendar.getInstance();
        updateDate();

        binding.nextDateBtn.setOnClickListener(c->{
            calendar.add(Calendar.DATE,1);
            updateDate();
        });
        binding.previousDateBtn.setOnClickListener(c-> {
                    calendar.add(Calendar.DATE, -1);
                    updateDate();
                });
        binding.floatingActionButton2.setOnClickListener(c->{
            new AddTransactionFragment().show(getSupportFragmentManager(),null);
        });

    }
    void updateDate(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        binding.currentDate.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}