package com.example.expensetracker.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.Model.Expense;
import com.example.expensetracker.R;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;
    private Context context;
    private ExpenseListener listener;


    public ExpenseAdapter(Context context, List<Expense> expenses, ExpenseListener listener) {
        this.context = context;
        this.expenseList = expenses;
        this.listener = listener;
    }

    public void updateList(List<Expense> newList) {
        this.expenseList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.txtName.setText(expense.getTitle());
        holder.txtAmount.setText("₹" + expense.getAmount());

        final int pos = position;
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(expense, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }


    private void showEditDialog(Expense expense, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Expense");

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_expense, null);
        EditText etName = dialogView.findViewById(R.id.editName);
        EditText etAmount = dialogView.findViewById(R.id.editAmount);


        etName.setText(expense.getTitle());
        etAmount.setText(String.valueOf(expense.getAmount()));

        builder.setView(dialogView);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = etName.getText().toString().trim();
                double newAmount = 0;
                try {
                    newAmount = Double.parseDouble(etAmount.getText().toString().trim());
                } catch (NumberFormatException e) {
                    newAmount = expense.getAmount();
                }


                expense.setTitle(newName);
                expense.setAmount(newAmount);

                notifyItemChanged(position);


                if (listener != null) listener.onExpenseEdited();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtAmount;
        ImageView btnEdit;

        ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtExpenseName);
            txtAmount = itemView.findViewById(R.id.txtExpenseAmount);
            btnEdit = itemView.findViewById(R.id.btnEdit); // from item_expense.xml
        }
    }


    public interface ExpenseListener {
        void onExpenseEdited();
    }
}
