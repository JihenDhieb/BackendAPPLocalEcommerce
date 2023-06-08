package com.example.AppEcommerce.Model;


import java.time.LocalDate;


public class RevenueDate {
    String id;
    int revenue;
    LocalDate date;

    public RevenueDate(int revenue, LocalDate date) {
        this.revenue = revenue;
        this.date = date;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


}
