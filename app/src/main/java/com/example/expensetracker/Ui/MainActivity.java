package com.example.expensetracker.Ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.Adapter.ExpenseAdapter;
import com.example.expensetracker.MainViewModel.ExpenseViewModel;
import com.example.expensetracker.Model.Expense;
import com.example.expensetracker.R;
import com.example.expensetracker.Utils.AppConstant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ExpenseViewModel expenseViewModel;
    private ExpenseAdapter expenseAdapter;
    private RecyclerView recyclerView;
    private TextView textTotal;
    private FloatingActionButton floatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        textTotal = findViewById(R.id.textTotal);
        floatingButton = findViewById(R.id.folatingButton);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        expenseAdapter = new ExpenseAdapter(new ArrayList<>());
        recyclerView.setAdapter(expenseAdapter);


        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);


        expenseViewModel.getExpense().observe(this, new Observer<ArrayList<Expense>>() {
            @Override
            public void onChanged(ArrayList<Expense> expenses) {
                expenseAdapter = new ExpenseAdapter(expenses);
                recyclerView.setAdapter(expenseAdapter);
            }
        });


        expenseViewModel.getTotal().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double total) {
                NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en","IN"));
                String formatedTotal = format.format(total)  ;
                textTotal.setText("Total: " + total);
            }
        });


        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddExpenseDialog();
            }
        });
    }

    private void showAddExpenseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(AppConstant.DIALOG_TITLE_ADD);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_expense, null);
        EditText editName = dialogView.findViewById(R.id.editName);
        EditText editAmount = dialogView.findViewById(R.id.editAmount);

        builder.setView(dialogView);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editName.getText().toString().trim();
                String amountText = editAmount.getText().toString().trim();

                if (!name.isEmpty() && !amountText.isEmpty()) {
                    try {
                        double amount = Double.parseDouble(amountText);
                        expenseViewModel.addExpense(name, amount);
                        Toast.makeText(MainActivity.this, AppConstant.MESSAGE_ADDED, Toast.LENGTH_SHORT).show();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this, AppConstant.MESSAGE_INVALID_INPUT, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }
}
