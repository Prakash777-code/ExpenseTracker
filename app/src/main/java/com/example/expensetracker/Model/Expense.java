package com.example.expensetracker.Model;

public class Expense {
    private String title;
    private Double amount;

    public Expense(String title, Double amount){
        this.title = title;
        this.amount = amount;
    }

    public String  getTitle(){
        return title;
    }

    public Double getAmount(){
        return amount;
    }
}
