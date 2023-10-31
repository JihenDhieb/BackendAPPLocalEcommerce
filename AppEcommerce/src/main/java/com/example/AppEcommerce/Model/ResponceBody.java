package com.example.AppEcommerce.Model;

import com.example.AppEcommerce.Dto.Products;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ResponceBody {
    @JsonProperty("recommended_products")
    private List<Products> recommended_products;

    public List<Products> getRecommendedProducts() {
        return recommended_products;
    }

    public void setRecommendedProducts(List<Products> recommendedProducts) {
        this.recommended_products = recommendedProducts;
    }
}