package com.example.AppEcommerce.Dto;

import com.example.AppEcommerce.Enum.Activity;
import com.example.AppEcommerce.Enum.Region;
import com.example.AppEcommerce.Model.File;
import com.example.AppEcommerce.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagesDto {
    @Id
    private String id;
    private String title;
    private String address;
    private String email;

    private String phone;
    private String postalCode;
    private File imageProfile;
    private File imageCouverture;

    private Activity activity;
    private Region region;
    private double longitude;

    private double latitude;

}
