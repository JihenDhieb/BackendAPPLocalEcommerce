package com.example.AppEcommerce.Impl;


import com.example.AppEcommerce.Dto.*;
import com.example.AppEcommerce.Model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AuthServiceImp {
    ResponseEntity<?> registerAdmin(SignUpAdmin signUpAdmin);

    String registerUser(SignUpUser signUpUser);


    ResponseEntity<?> registerDelivery(SignUpDelivery signUpDelivery);

    JwtResonse login(LoginRequest loginRequest);


    ResponseEntity<?> registerSousAdmin(SignUpSousAdmin signUpAdmin);
}
