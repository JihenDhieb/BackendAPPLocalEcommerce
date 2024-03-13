package com.example.AppEcommerce.Dto;

import com.example.AppEcommerce.Model.User;
import com.example.AppEcommerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class JwtResonse {
    @Autowired
    private UserRepository userRepository;
    private String token;
    private String email;
    private String type = "Bearer";
    private String  id;

    private List<String> role;
    public JwtResonse(String accessToken, User user,
                       List<String> role) {
        this.token = accessToken;
        this.id = user.getId();
        this.email = user.getEmail();
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }
}
