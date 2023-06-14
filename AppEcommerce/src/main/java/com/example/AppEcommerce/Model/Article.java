package com.example.AppEcommerce.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import javax.persistence.*;


@Data


public class Article {
    @Id
    private String id;
    private String nom;
    private String description;
    private String prix;
    private String  nbstock;



    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private File image;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "page_id")
    private Pages page;
    public Article(){};

    public Article(String nom, String description, String prix, String nbstock, Pages page) {
        this.nom=nom;
        this.description=description;
        this.prix=prix;
        this.nbstock=nbstock;
        this.page=page;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getNbstock() {
        return nbstock;
    }

    public void setNbstock(String nbstock) {
        this.nbstock = nbstock;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public Pages getPage() {
        return page;
    }

    public void setPage(Pages page) {
        this.page = page;
    }
}
