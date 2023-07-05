package com.example.AppEcommerce.Model;

import com.example.AppEcommerce.Enum.Activity;
import com.example.AppEcommerce.Enum.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor

public class Pages {
    @Id
    private String id;
    private String title;
    private String address;
    private String email;
    private String phone ;
    private String  postalCode;
     @Enumerated(EnumType.STRING)
     private Activity activity;
    @Enumerated(EnumType.STRING)
    private Region region;

    private double longitude;

    private double latitude;
    private boolean enLigne;

    public boolean isEnLigne() {
        return enLigne;
    }

    public void setEnLigne(boolean enLigne) {
        this.enLigne = enLigne;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_profil_id")
    private File imageProfile;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_couverture_id")
    private File imageCouverture;

    public Pages(){}
    public Pages(String title, String address, String email,String phone,String  postalCode, Activity activity,Region region, double longitude, double latitude) {
        this.title=title;
        this.address=address;
        this.phone=phone;
        this.postalCode=postalCode;
        this.email=email;
        this.activity=activity;
        this.region=region;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public File getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(File imageProfile) {
        this.imageProfile = imageProfile;
    }

    public File getImageCouverture() {
        return imageCouverture;
    }

    public void setImageCouverture(File imageCouverture) {
        this.imageCouverture = imageCouverture;
    }

    public Activity getActivity() {
        return activity;
    }
    public Region getRegion() {
        return region;
    }
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

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


}
