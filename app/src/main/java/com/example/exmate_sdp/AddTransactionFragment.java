     package com.example.exmate_sdp;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.exmate_sdp.databinding.FragmentAddTransactionBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;


     public class AddTransactionFragment extends BottomSheetDialogFragment {


    public AddTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    FragmentAddTransactionBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAddTransactionBinding.inflate(inflater);
        binding.incomeBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackgroundResource(R.drawable.income_selector);
            binding.expenseBtn.setBackgroundResource(R.drawable.default_selector);
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.greenColor));

        });
        binding.expenseBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackgroundResource(R.drawable.default_selector);
            binding.expenseBtn.setBackgroundResource(R.drawable.expense_selector );
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.redColor));

        });
        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext());
                datePickerDialog.setOnDateSetListener((datePicker, i, i1, i2) -> {
                    Calendar calendar=Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
                    calendar.set(Calendar.MONTH,datePicker.getMonth());
                    calendar.set(Calendar.YEAR,datePicker.getYear());

                    SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
                    String dateToShow=dateFormat.format(calendar.getTime());
                    binding.date.setText(dateToShow);


                });
                datePickerDialog.show();
            }
        });

        return binding.getRoot();
    }
}