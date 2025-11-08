package com.example.exmate_sdp.views.activites;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.exmate_sdp.R;
import com.example.exmate_sdp.adapters.TransactionsAdapter;
import com.example.exmate_sdp.databinding.ActivityMainBinding;
import com.example.exmate_sdp.models.Transaction;
import com.example.exmate_sdp.utils.Constants;
import com.example.exmate_sdp.utils.Helper;
import com.example.exmate_sdp.views.fragments.AddTransactionFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        Constants.setCategories();
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

        ArrayList<Transaction> transactions=new ArrayList<>();
        transactions.add(new Transaction(Constants.INCOME,"Salary","Cash","",new Date(),5000.00,1));
        transactions.add(new Transaction(Constants.EXPENSE,"Investment","Bank","",new Date(),-1000.00,2));
        transactions.add(new Transaction(Constants.INCOME,"Rent","Cash","",new Date(),3000.00,3));
        transactions.add(new Transaction(Constants.EXPENSE,"Business","Cash","",new Date(),-2000.00,4));

        TransactionsAdapter transactionsAdapter=new TransactionsAdapter(this,transactions);
        binding.transactionList.setLayoutManager(new LinearLayoutManager(this));
        binding.transactionList.setAdapter(transactionsAdapter);


    }
    void updateDate(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}