package com.example.AppEcommerce.Model;

import java.time.LocalDate;

public class BenifitsVendor {
    String id;
  double benefits;
    private double chiffre;
    private double frais;
    LocalDate date;
public BenifitsVendor(double benefits,double chiffre,double  frais,LocalDate date){
    this.benefits=benefits;
    this.chiffre=chiffre;
    this.frais=frais;
    this.date=date;

}
    public double  getBenefits() {
        return benefits;
    }

    public void setBenefits(double  benefits) {
        this.benefits = benefits;
    }

    public double getChiffre() {
        return chiffre;
    }

    public void setChiffre(double chiffre) {
        this.chiffre= chiffre;
    }

    public double  getFrais() {
        return frais;
    }

    public void setFrais(double  frais) {
        this.frais = frais;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


}
