package com.example.AppEcommerce.Controller;

import com.example.AppEcommerce.Dto.LoginRequest;
import com.example.AppEcommerce.Dto.SignUpAdmin;
import com.example.AppEcommerce.Dto.SignUpDelivery;
import com.example.AppEcommerce.Dto.SignUpUser;
import com.example.AppEcommerce.Service.AuthService;
import com.example.AppEcommerce.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/auth")// tnajm t7el il port wala ta5u il permission mn springsecurity config
public class AuthController {
    @Autowired
    AuthService authService;



    @PostMapping("/registerAdmin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody SignUpAdmin signUpAdmin){
        return authService.registerAdmin(signUpAdmin);
    }
    @PostMapping("/loginUser")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }
    @PostMapping("/registerUser")
    public String registerUser(@Valid @RequestBody SignUpUser signUpUser){
        return authService.registerUser(signUpUser);
    }

    @PostMapping("/registerDelivery")
    public ResponseEntity<?> registerDelivery(@Valid @RequestBody SignUpDelivery signUpDelivery){
        return authService.registerDelivery(signUpDelivery);
    }

}
