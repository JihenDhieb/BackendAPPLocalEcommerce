package com.example.AppEcommerce.Dto;

import com.example.AppEcommerce.Model.ArticleCaisse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaisseDto {
    @Id
    String id;
    String idSender;
    String address;
    String streetAddress;
    String phone;
    String selectedTime;
    String description;
    String idPage;
    Double subTotal;
   String frais ;
    Double totalPrice;
    List<ArticleCaisse> articles;
}
