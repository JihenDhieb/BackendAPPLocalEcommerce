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
public class StoryDto {
       @Id
       private String id;
       private String namearticle;
       private String description;
       private int nbarticle;
       private double prix;
       private File image;
       private Pages page;

}
