package com.example.AppEcommerce.Model;

public class ArticleCaisse {
    String idArticle;
    int qnt;

    public ArticleCaisse(String idArticle, int qnt) {
        this.idArticle = idArticle;
        this.qnt = qnt;
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
