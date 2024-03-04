package com.example.AppEcommerce.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Products {
    private  String id;
    private String nom;
    private String description;
    private String prix;

    public Products( String nom, String description,String prix) {

        this.nom = nom;
        this.description = description;
        this.prix= prix;
    }
}