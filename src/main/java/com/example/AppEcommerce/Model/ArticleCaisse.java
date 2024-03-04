package com.example.AppEcommerce.Model;

public class ArticleCaisse {
    String idArticle;
    int qnt;
    private String  prix;

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getAvis() {
        return avis;
    }

    public void setAvis(String avis) {
        this.avis = avis;
    }

   String  avis;
    

    public ArticleCaisse(String idArticle, int qnt,String avis) {
        this.idArticle = idArticle;
        this.qnt = qnt;
        this.avis= avis;
    }

    public String getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(String idArticle) {
        this.idArticle = idArticle;
    }

    public int getQnt() {
        return qnt;
    }

    public void setQnt(int qnt) {
        this.qnt = qnt;
    }
}
