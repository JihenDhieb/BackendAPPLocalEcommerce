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
    private boolean etat;
    private List<History> productId;
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




    public User(String email, String password, Role role, String firstName, String lastName,String phone,String ville) {
        this.email=email;
        this.password=password;
        this.firstName=firstName;
        this.lastName=lastName;
        this.role=role;
        this.ville=ville;
        this.phone=phone;

    }
private double commissiontotale;

    public List<String> getHistoriquesRecherche() {
        return historiquesRecherche;
    }

    public void setHistoriquesRecherche(List<String> historiquesRecherche) {
        this.historiquesRecherche = historiquesRecherche;
    }

    private List<String> historiquesRecherche =new ArrayList<>();
    public List<BenifitsVendor> getBenifitsVendors() {
        return benifitsVendors;
    }

    public void setBenifitsVendors(List<BenifitsVendor> benifitsVendors) {
        this.benifitsVendors = benifitsVendors;
    }




    private boolean enLigne;

    private double longitude;

    private double latitude;
    private String ville;
    private double compteurC;
    private double t;

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



    public User(String email, String password, Role role, String firstName, String lastName,String phone){
        this.email=email;
        this.password=password;
        this.firstName=firstName;
        this.lastName=lastName;
        this.role=role;
        this.phone=phone;


    }
    public User(String email, String password, String firstName, String lastName, String phone, Role role,  double commissiontotale, boolean enLigne,double longitude, double latitude){
        this.email=email;
        this.password=password;
        this.firstName=firstName;
        this.lastName=lastName;
        this.phone=phone;
        this.role=role;
        this.commissiontotale =commissiontotale;
        this.enLigne = enLigne;
        this.longitude = longitude;
        this.latitude = latitude;

    }


    public boolean isPresent() {
        return true;
    }

}
