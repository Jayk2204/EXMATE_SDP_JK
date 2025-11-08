     package com.example.exmate_sdp.views.fragments;

     import android.app.AlertDialog;
     import android.app.DatePickerDialog;
     import android.os.Bundle;
     import android.view.LayoutInflater;
     import android.view.View;
     import android.view.ViewGroup;

     import androidx.recyclerview.widget.DividerItemDecoration;
     import androidx.recyclerview.widget.GridLayoutManager;
     import androidx.recyclerview.widget.LinearLayoutManager;

     import com.example.exmate_sdp.R;
     import com.example.exmate_sdp.adapters.AccountsAdapter;
     import com.example.exmate_sdp.adapters.CategoryAdapter;
     import com.example.exmate_sdp.databinding.FragmentAddTransactionBinding;
     import com.example.exmate_sdp.databinding.ListDialogBinding;
     import com.example.exmate_sdp.models.Account;
     import com.example.exmate_sdp.models.Category;
     import com.example.exmate_sdp.utils.Constants;
     import com.example.exmate_sdp.utils.Helper;
     import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

     import java.text.SimpleDateFormat;
     import java.util.ArrayList;
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
                    String dateToShow= Helper.formatDate(calendar.getTime());
                    binding.date.setText(dateToShow);


                });
                datePickerDialog.show();
            }
        });
        binding.category.setOnClickListener(c->
        {
            ListDialogBinding dialogBinding=ListDialogBinding.inflate(inflater);
            AlertDialog categoryDialog= new AlertDialog.Builder(getContext()).create();
            categoryDialog.setView(dialogBinding.getRoot());



            CategoryAdapter categoryAdapter=new CategoryAdapter(getContext(), Constants.categories, new CategoryAdapter.CategoryClickListner() {
                @Override
                public void onCategoryClicked(Category category) {
                    binding.category.setText(category.getCategoryName());
                    categoryDialog.dismiss();
                }
            });
            dialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
            dialogBinding.recyclerView.setAdapter(categoryAdapter);
            categoryDialog.show();



        });

        binding.account.setOnClickListener(c->
        {
            ListDialogBinding dialogBinding=ListDialogBinding.inflate(inflater);
            AlertDialog accountsDialog= new AlertDialog.Builder(getContext()).create();
            accountsDialog.setView(dialogBinding.getRoot());

            ArrayList<Account> accounts=new ArrayList<>();
            accounts.add(new Account(0,"Cash"));
            accounts.add(new Account(0,"Credit Card"));
            accounts.add(new Account(0,"Debit Card"));
            accounts.add(new Account(0,"Paytm"));
            accounts.add(new Account(0,"Others"));

            AccountsAdapter adapter=new AccountsAdapter(getContext(), accounts, new AccountsAdapter.AccountsClickListner() {
                @Override
                public void onAccountSelected(Account account) {
                    binding.account.setText(account.getAccountName());
                    accountsDialog.dismiss();
                }
            });
            dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            dialogBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
            dialogBinding.recyclerView.setAdapter(adapter);
            accountsDialog.show();

        });

        return binding.getRoot();
    }
}