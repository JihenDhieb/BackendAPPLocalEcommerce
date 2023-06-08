package com.example.AppEcommerce.Service;



import com.example.AppEcommerce.Dto.*;
import com.example.AppEcommerce.Enum.Role;
import com.example.AppEcommerce.Impl.AuthServiceImp;
import com.example.AppEcommerce.Model.User;
import com.example.AppEcommerce.Repository.FileRepository;
import com.example.AppEcommerce.Repository.UserRepository;
import com.example.AppEcommerce.Security.UserDetailsImpl;
import com.example.AppEcommerce.Security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AuthService implements AuthServiceImp {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    FileRepository fileRepository;

    @Override
    public ResponseEntity<?> registerAdmin(SignUpAdmin signUpAdmin){
        if (userRepository.existsByEmail(signUpAdmin.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error:email is already taken!"));
        }

        User user =new User( signUpAdmin.getEmail(),encoder.encode(signUpAdmin.getPassword()), Role.ADMIN);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("Admin registered successfully!"));

    }
    @Override
    public String registerUser(SignUpUser signUpUser){
        if (userRepository.existsByEmail(signUpUser.getEmail())) {
            String message = String.valueOf(new MessageResponse("Error:email is already taken!"));
            return message;
        }

        User user =new User( signUpUser.getEmail(),encoder.encode(signUpUser.getPassword()), Role.CLIENT,signUpUser.getFirstName(),signUpUser.getLastName(),signUpUser.getPhone(),signUpUser.getCin());
        User user1 = userRepository.save(user);
        return user1.getId();

    }

    @Override
    public ResponseEntity<?> registerDelivery(SignUpDelivery signUpDelivery){
        if (userRepository.existsByEmail(signUpDelivery.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error:email is already taken!"));
        }
        User user =new User( signUpDelivery.getEmail(),
                encoder.encode(signUpDelivery.getPassword()), signUpDelivery.getFirstName(),
                signUpDelivery.getLastName(), signUpDelivery.getPhone(),
                Role.DELIVERY,signUpDelivery.getSold(),
                true,signUpDelivery.getLongitude(),
                signUpDelivery.getLatitude());
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("Delivery registered successfully!"));
    }

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        User user = userRepository.findById(userDetails.getId()).get();
        return ResponseEntity.ok(new JwtResonse(jwt,
                user,
                roles));


    }

}
