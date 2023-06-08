package com.example.AppEcommerce.Dto;

import com.example.AppEcommerce.Model.File;
import com.example.AppEcommerce.Model.Pages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class getArticleCaisseDto {
    private String nom;
    private String prix;
   int  qnt;
    private File image;
    private Pages page;
}
