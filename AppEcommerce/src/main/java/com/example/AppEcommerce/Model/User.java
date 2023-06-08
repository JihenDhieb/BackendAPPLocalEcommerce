package com.example.AppEcommerce.Model;


import com.example.AppEcommerce.Enum.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

    @Id

     private String id;



    @Email
    private String email;

    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Pages> pages =new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,orphanRemoval = true)
    private List<RevenueDate> revenueDates =new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,orphanRemoval = true)
    private List<BenifitsVendor> benifitsVendors =new ArrayList<>();


    public List<BenifitsVendor> getBenifitsVendors() {
        return benifitsVendors;
    }

    public void setBenifitsVendors(List<BenifitsVendor> benifitsVendors) {
        this.benifitsVendors = benifitsVendors;
    }

    private int sold;


    private boolean enLigne;

    private double longitude;

    private double latitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    private String cin;

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }



    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_profil_id")
    private File imageProfile;

    public File getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(File imageProfile) {
        this.imageProfile = imageProfile;
    }

    public User(String email, String password, Role role ) {
        this.email=email;
        this.password=password;
        this.role=role;
    }


    public int  getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }
    public User(String email, String password, Role role, String firstName, String lastName,String phone,String cin) {
        this.email=email;
        this.password=password;
        this.firstName=firstName;
        this.lastName=lastName;
        this.role=role;
        this.phone=phone;
        this.cin=cin;

    }
    public User(String email, String password, String firstName, String lastName, String phone, Role role,  int sold, boolean enLigne,double longitude, double latitude){
        this.email=email;
        this.password=password;
        this.firstName=firstName;
        this.lastName=lastName;
        this.phone=phone;
        this.role=role;
        this.sold = sold;
        this.enLigne = enLigne;
        this.longitude = longitude;
        this.latitude = latitude;

    }


    public boolean isPresent() {
        return true;
    }

}
