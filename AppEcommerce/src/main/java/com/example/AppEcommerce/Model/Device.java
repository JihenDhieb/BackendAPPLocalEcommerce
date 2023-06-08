package com.example.AppEcommerce.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Data
public class Device {
    @Id
    private String id;
    @Lob
    private String token;
    @ManyToOne
    private User user;
    public Device(){}
    public Device(String token){this.token=token;}

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
