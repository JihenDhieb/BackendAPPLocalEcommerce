package com.example.AppEcommerce.Impl;


import com.example.AppEcommerce.Dto.LoginRequest;
import com.example.AppEcommerce.Dto.SignUpAdmin;
import com.example.AppEcommerce.Dto.SignUpDelivery;
import com.example.AppEcommerce.Dto.SignUpUser;
import com.example.AppEcommerce.Model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AuthServiceImp {
    ResponseEntity<?> registerAdmin(SignUpAdmin signUpAdmin);

    String registerUser(SignUpUser signUpUser);


    ResponseEntity<?> registerDelivery(SignUpDelivery signUpDelivery);

    ResponseEntity<?> login(LoginRequest loginRequest);


}
