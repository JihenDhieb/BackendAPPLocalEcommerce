package com.example.AppEcommerce.Dto;


import com.example.AppEcommerce.Model.File;
import com.example.AppEcommerce.Model.Pages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {
    @Id
    private String id;
    private String nom;
    private String description;
    private String  prix;
    private String nbstock;
    private File image;
    private Pages page;
}
