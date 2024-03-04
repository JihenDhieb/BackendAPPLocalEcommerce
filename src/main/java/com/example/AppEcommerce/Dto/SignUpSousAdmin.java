package com.example.AppEcommerce.Dto;

import com.example.AppEcommerce.Enum.Role;
import com.google.firebase.database.annotations.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpSousAdmin {
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
    private Role role;
}
