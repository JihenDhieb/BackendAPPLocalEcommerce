package com.example.AppEcommerce.Controller;

import com.example.AppEcommerce.Dto.*;
import com.example.AppEcommerce.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public JwtResonse login(@Valid @RequestBody LoginRequest loginRequest){
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
    @PostMapping("/SousAdmin")
    public ResponseEntity<?> SousAd(@Valid @RequestBody SignUpSousAdmin signUpSousAdmin){
        return authService.registerSousAdmin(signUpSousAdmin);
    }

}
