package com.example.AppEcommerce.Model;

import java.time.LocalDate;

public class BenifitsVendor {
    String id;
    int benefits;
    private double chiffre;
    private int frais;
    LocalDate date;
public BenifitsVendor(int benefits,double chiffre,int frais,LocalDate date){
    this.benefits=benefits;
    this.chiffre=chiffre;
    this.frais=frais;
    this.date=date;

}
    public int getBenefits() {
        return benefits;
    }

    public void setBenefits(int benefits) {
        this.benefits = benefits;
    }

    public double getChiffre() {
        return chiffre;
    }

    public void setChiffre(double chiffre) {
        this.chiffre= chiffre;
    }

    public int getFrais() {
        return frais;
    }

    public void setFrais(int frais) {
        this.frais = frais;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


}
