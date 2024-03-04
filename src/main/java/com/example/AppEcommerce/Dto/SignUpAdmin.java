package com.example.AppEcommerce.Dto;

import com.example.AppEcommerce.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpAdmin {
    private String id;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
    private Role role;
}
