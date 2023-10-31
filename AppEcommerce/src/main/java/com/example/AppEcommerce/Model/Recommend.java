package com.example.AppEcommerce.Model;

import com.example.AppEcommerce.Dto.Products;

import java.util.List;

public class Recommend{
    private List<Products> products;
    private List<History> history;

    public Recommend(List<Products> products, List<History> history) {
        this.products = products;
        this.history = history;
    }

    public List<Products> getProducts() {
        return products;
    }

    public List<History> getHistory() {
        return history;
    }
}
