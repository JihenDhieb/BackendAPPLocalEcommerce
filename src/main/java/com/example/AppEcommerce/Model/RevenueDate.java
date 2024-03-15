package com.example.AppEcommerce.Model;


import java.time.LocalDate;


public class RevenueDate {
    String id;
    double revenue;
    LocalDate date;

    public RevenueDate(double revenue, LocalDate date) {
        this.revenue = revenue;
        this.date = date;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


}
