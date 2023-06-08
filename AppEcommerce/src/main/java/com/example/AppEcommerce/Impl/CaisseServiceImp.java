package com.example.AppEcommerce.Impl;

import com.example.AppEcommerce.Dto.CaisseDto;
import com.example.AppEcommerce.Dto.getArticleCaisseDto;
import com.example.AppEcommerce.Model.ArticleCaisse;
import com.example.AppEcommerce.Model.Caisse;
import com.example.AppEcommerce.Model.RevenueDate;
import com.example.AppEcommerce.Model.User;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.time.LocalDate;
import java.util.List;

public interface CaisseServiceImp {

    String addCaisse(CaisseDto caisseDto) throws FirebaseMessagingException;

    void sendCaisseNotif(String id) throws FirebaseMessagingException;


    Caisse getCaisseById(String id);


    List<getArticleCaisseDto> getArticlesCaisse(List<ArticleCaisse> articleCaisses);

    void addDeliveryToCaisse(String idCaisse, String idDelivery) throws FirebaseMessagingException;

    List<Caisse> caisseListDelivery(String idDelivery);

    void CancelByVendor(String idCaisse) throws FirebaseMessagingException;

    void AcceptByVendor(String idCaisse) throws FirebaseMessagingException;

    List<Caisse> caisseListClient(String idSender);

    List<Caisse> caisseListVendor(String idSender);

    void CancelByClient(String idCaisse) throws FirebaseMessagingException;

    void SetStatutBeingdelivred(String idCaisse);

    void SetStatutdelivred(String idCaisse);

    void SetSold(String idCaisse);


    LocalDate getDateByIdCaisse(String idCaisse);


    List<Caisse> getAllCaisse();

    int getDeliveredCountForDelivery(String id);

    int getCancelCountForDelivery(String id);

    int getNbCaisseForDelivery(String id);

    int getNbCaisseForVendor(String id);

    int calculateTotalRevenueForAllCaisse(String id);



    int getDeliveredCountForVendor(String id);

    int getCancelCountForVendor(String id);

    int countClients(String id);

 
    void benefitsVendor(String id);

    List<User> getListClientByVendor(String id);
}
