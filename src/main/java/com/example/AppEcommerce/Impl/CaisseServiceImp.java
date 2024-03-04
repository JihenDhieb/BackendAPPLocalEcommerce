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
import java.util.Map;

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


    double calculateTotalCommissionAndFrais(String userId);

    LocalDate getDateByIdCaisse(String idCaisse);


    List<Caisse> getAllCaisse();

    int getDeliveredCountForDelivery(String id);

    int getCancelCountForDelivery(String id);

    int getNbCaisseForDelivery(String id);

    int getNbCaisseForVendor(String id);

    double  calculateTotalRevenueForAllCaisse(String id);



    int getDeliveredCountForVendor(String id);

    int getCancelCountForVendor(String id);

    int countClients(String id);

 


    List<User> getListClientByVendor(String id);

    String addAvis(String idArticle, String idSender, String avis);





    List<String> getAvisParArticle(String idArticle);


    void setStatutDelivered(String idCaisse);

    double fraisDelivery(String idCaisse);

    double calculateDistance(double lat1, double lon1, double lat2, double lon2);

    //Admin

    List<String> getListPageParCommande(String id);

    //update solde pour sous admin
    void UpdateSoldeSousAdmin(String id, String id2, int i);

    //update solde pour sous admin (carger le page)
    void PageUpdateSolde(String id, String id2, int i);

    //reset sous admin
    void Reset(String id);

    void payer(String id, String ids);

    //update etat
    void UpdateEtat(String id);

    int todaysales();

    //total sales
    Double totalsales();

    int SalesARTToday();

    int SalesART();

    int revenuLivreurT();

    //nbre de livraison total
    int revenuLivreur();

    //revenu admin today
    double adminRevenu();

    double AdmeinRedvenuTotal();
}
