package com.example.AppEcommerce.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class editLongLatUser {
    String id;
    double latitude;
    double longitude;
}
