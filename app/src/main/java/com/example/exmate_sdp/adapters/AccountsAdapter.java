package com.example.exmate_sdp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exmate_sdp.R;
import com.example.exmate_sdp.databinding.RowAccountBinding;
import com.example.exmate_sdp.models.Account;

import java.util.ArrayList;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder> {

    Context context;
    ArrayList<Account>  accountsArrayList;

    public  interface AccountsClickListner{
        void onAccountSelected(Account account);
    }
   AccountsClickListner accountsClickListner;
   public AccountsAdapter(Context context, ArrayList<Account> accountsArrayList,AccountsClickListner accountsClickListner)
   {
        this.context=context;
        this.accountsArrayList=accountsArrayList;
        this.accountsClickListner=accountsClickListner;
   }
    @NonNull
    @Override
    public AccountsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountsViewHolder(LayoutInflater.from(context).inflate(R.layout.row_account,parent,false) );
    }

    @Override
    public void onBindViewHolder(@NonNull AccountsViewHolder holder, int position) {
        Account account=accountsArrayList.get(position);
        holder.binding.accountName.setText(account.getAccountName());
        holder.itemView.setOnClickListener(c->{
            accountsClickListner.onAccountSelected(account);
        });

    }

    @Override
    public int getItemCount() {
        return accountsArrayList.size();
    }

    public class AccountsViewHolder extends RecyclerView.ViewHolder{
        RowAccountBinding binding;
        public AccountsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=RowAccountBinding.bind(itemView);
        }
    }
}
