package com.example.AppEcommerce.Dto;

import com.example.AppEcommerce.Enum.Role;
import com.example.AppEcommerce.Enum.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDelivery {
    private String id;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String ville;
    @Enumerated(EnumType.STRING)
    private Role role = Role.DELIVERY;
    private int revenue = 0;
    private double commissiontotale = 0;
    private boolean enLigne;
    private double longitude;
    private double latitude;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus = UserStatus.ACTIVE;
    private int cin;

}
