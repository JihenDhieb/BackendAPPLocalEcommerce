package com.example.AppEcommerce.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
@Data

public class Story {
    @Id
    private  String id;
    private String namearticle;
    private String description;
    private int nbarticle;
    private double prix ;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private File image;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "page_id")
    private Pages page;
    public Story(String namearticle,String description,int nbarticle,double prix,Pages page){
        this.namearticle=namearticle;
        this.description=description;
        this.nbarticle=nbarticle;
        this.prix=prix;
        this.page=page;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Story(){}
    public String getNamearticle() {
        return namearticle;
    }

    public void setNamearticle(String namearticle) {
        this.namearticle = namearticle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNbarticle() {
        return nbarticle;
    }

    public void setNbarticle(int nbarticle) {
        this.nbarticle = nbarticle;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
}
