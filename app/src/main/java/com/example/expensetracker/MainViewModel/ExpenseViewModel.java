package com.example.expensetracker.MainViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.expensetracker.Model.Expense;

import java.util.ArrayList;

public class ExpenseViewModel extends ViewModel {

  private MutableLiveData<ArrayList<Expense>>  expenses = new MutableLiveData(new ArrayList<Expense>());
  private MutableLiveData<Double> total = new MutableLiveData<>(0.0);

  public LiveData<ArrayList<Expense>> getExpense(){
      return expenses;
  }

  public LiveData<Double> getTotal(){
      return total;
  }

  public void addExpense(String title, double amount){
      ArrayList<Expense> currentList = expenses.getValue();
      if(currentList != null){
          currentList.add(new Expense(title, amount));
          expenses.setValue(currentList);
      }

      Double currentTotal = total.getValue();
      if(currentTotal != null){
          total.setValue(currentTotal+amount);
      }
  }

}
