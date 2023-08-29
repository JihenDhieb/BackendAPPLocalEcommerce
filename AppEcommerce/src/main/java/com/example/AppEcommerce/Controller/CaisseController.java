package com.example.AppEcommerce.Controller;

import com.example.AppEcommerce.Dto.CaisseDto;
import com.example.AppEcommerce.Dto.PushNotificationRequest;
import com.example.AppEcommerce.Dto.getArticleCaisseDto;
import com.example.AppEcommerce.Enum.Status;
import com.example.AppEcommerce.Model.*;
import com.example.AppEcommerce.Repository.CaisseRepository;
import com.example.AppEcommerce.Service.CaisseService;
import com.example.AppEcommerce.Service.NotificationService;
import com.example.AppEcommerce.Service.UserService;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/caisse")
public class CaisseController {
    @Autowired
    CaisseService caisseService;
    @Autowired
    NotificationService notificationService;
    CaisseRepository caisseRepository;
    @Autowired
    public CaisseController(CaisseRepository caisseRepository) {
        this.caisseRepository = caisseRepository;
    }

    @PostMapping(value = "/add")
    public String addCaisse(@RequestBody CaisseDto caisseDto) throws FirebaseMessagingException {
        return caisseService.addCaisse( caisseDto);
    }
    @GetMapping("/caisseNotif/{id}")
    public ResponseEntity<String> sendNotification(@PathVariable String id) {
        try {
            caisseService.sendCaisseNotif(id);
            return ResponseEntity.ok("Notification sent successfully");
        } catch (FirebaseMessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send notification: " + e.getMessage());
        }
    }
    @GetMapping(value = "/getCaisse/{id}")
    public Caisse getCaisseById(@PathVariable String id) {
        return caisseService.getCaisseById(id);
    }

    @PostMapping(value = "/getCaisseArticles")
    public List<getArticleCaisseDto> getCaisseArticles(@RequestBody List<ArticleCaisse> caisses) {
        return caisseService.getArticlesCaisse(caisses);
    }
    @GetMapping(value = "/addDeliveryToCaisse/{idCaisse}/{idDelivery}")
    public void addDeliveryToCaisse(@PathVariable String idCaisse,
                                    @PathVariable String idDelivery) throws FirebaseMessagingException {
        caisseService.addDeliveryToCaisse(idCaisse, idDelivery);
    }
    @GetMapping(value = "/caisseListDelivery/{id}")
    public List<Caisse> caisseListDelivery(@PathVariable String id) {
        return caisseService.caisseListDelivery(id);
    }
    @GetMapping(value = "/caisseListClient/{id}")
    public List<Caisse> caisseListClient(@PathVariable String id) {
        return caisseService.caisseListClient(id);
    }
    @GetMapping(value = "/caisseListVendor/{id}")
    public List<Caisse> caisseListVendor(@PathVariable String id) {
        return caisseService.caisseListVendor(id);
    }
    @GetMapping(value = "/cancelOrderByVendor/{id}")
    public void cancelOrderByVendor(@PathVariable String id) throws FirebaseMessagingException {
        caisseService.CancelByVendor(id);
    }
    @GetMapping(value = "/AcceptOrderByVendor/{id}")
    public void AcceptOrderByVendor(@PathVariable String id) throws FirebaseMessagingException {
        caisseService.AcceptByVendor(id);
    }


    @GetMapping(value = "/cancelOrderByClient/{id}")
    public void cancelOrderByClient(@PathVariable String id) throws FirebaseMessagingException {
        caisseService.CancelByClient(id);
    }
    @GetMapping(value = "/SetStatutBeingdelivred/{id}")
    public void SetStatutBeingdelivred(@PathVariable String id) throws FirebaseMessagingException {
        caisseService.SetStatutBeingdelivred(id);
    }
    @GetMapping(value = "/SetStatutdelivred/{id}")
    public void SetStatutdelivred(@PathVariable String id) throws FirebaseMessagingException {
        caisseService.setStatutDelivered(id);
    }
    @GetMapping("/{idCaisse}/date")
    public ResponseEntity<LocalDate> getCaisseDateById(@PathVariable String idCaisse) {
        LocalDate date = caisseService.getDateByIdCaisse(idCaisse);
        if (date != null) {
            return ResponseEntity.ok(date);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}/delivered-count")
    public ResponseEntity<Integer> getDeliveredCountForDelivery(@PathVariable("id") String id) {
        int deliveredCount = caisseService.getDeliveredCountForDelivery(id);
        return ResponseEntity.ok(deliveredCount);
    }
    @GetMapping("/{id}/delivered-count-vendor")
    public ResponseEntity<Integer> getDeliveredCountForVendor(@PathVariable("id") String id) {
        int deliveredCount = caisseService.getDeliveredCountForVendor(id);
        return ResponseEntity.ok(deliveredCount);
    }
    @GetMapping("/{id}/cancel-count")
    public ResponseEntity<Integer> getCancelCountForDelivery(@PathVariable("id") String id) {
        int cancelCount = caisseService.getCancelCountForDelivery(id);
        return ResponseEntity.ok(cancelCount);
    }
    @GetMapping("/{id}/cancel-count-vendor")
    public ResponseEntity<Integer> getCancelCountForVendor(@PathVariable("id") String id) {
        int cancelCount = caisseService.getCancelCountForVendor(id);
        return ResponseEntity.ok(cancelCount);
    }
    @GetMapping("/{id}/caisses/count")
    public int getNumberOfCaisseForDelivery(@PathVariable String id) {
        return caisseService.getNbCaisseForDelivery(id);
    }
    @GetMapping("/{id}/caisses/count-vendor")
    public int getNumberOfCaisseForVendor(@PathVariable String id) {
        return caisseService.getNbCaisseForVendor(id);
    }

    @GetMapping("/{id}/revenue/total")
    public double  calculateTotalRevenueForCaisse(@PathVariable("id") String caisseId) {
        return caisseService.calculateTotalRevenueForAllCaisse(caisseId);
    }

    @GetMapping("/caisses/{id}/countclient")
    public int countClients(@PathVariable String id) {
        return caisseService.countClients(id);
    }


    @GetMapping("/calculateBenefitsVendor/{userId}")
    public ResponseEntity<String> calculateBenefitsVendor(@PathVariable String userId) {
       caisseService.benefitsVendor(userId);
        return ResponseEntity.ok("Calcul des bénéfices pour le vendeur effectué avec succès.");
    }
    @GetMapping("/{vendorId}/totalchiffre")
    public double totalchiffre(@PathVariable String vendorId) {
        return caisseService.calculateTotalRevenue(vendorId);
    }
    @GetMapping("/{vendorId}/totalfrais")
    public double totalfrais(@PathVariable String vendorId) {
        return caisseService.calculateTotalFrais(vendorId);
    }
    @GetMapping("/{vendorId}/totalbenifits")
    public double totalbenifits(@PathVariable String vendorId) {
        return caisseService.calculateTotalbenifits(vendorId);
    }
    @GetMapping("/clients/{id}")
    public List<User> getClientsByVendor(@PathVariable("id") String id) {
        return caisseService.getListClientByVendor(id);
    }

    @PostMapping("/send-notification")
    public ResponseEntity<String> sendNotificationForDeliveredCaisse() {
        try {
            notificationService.sendClientNotificationForDeliveredCaisse();
            return ResponseEntity.ok("Notifications sent successfully.");
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send notifications.");
        }
    }
    @PostMapping("/avis/{idArticle}/{idSender}")
    public ResponseEntity<String> addAvis(
            @PathVariable String idArticle,
            @PathVariable String idSender,
            @RequestBody String avis) {
        try {
            String updatedArticleId = caisseService.addAvis(idArticle,idSender, avis);
            return ResponseEntity.ok(updatedArticleId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{idArticle}/avis")
    public ResponseEntity<List<String>> afficherAvisParArticle(@PathVariable String idArticle) {
        List<String> avisList = caisseService.getAvisParArticle(idArticle);

        if (!avisList.isEmpty()) {
            return ResponseEntity.ok(avisList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/totalFrais/{idDelivery}")
    public double totalFrais(@PathVariable String idDelivery){return caisseService.fraisTotal(idDelivery);}
    @GetMapping("/commission/{totalFrais}")
    public double commission(@PathVariable double totalFrais){return caisseService.comissionFrais(totalFrais);}
    @GetMapping("/commission1/{userId}")
    public ResponseEntity<Double> getCommissionAndFrais(@PathVariable String userId) {
        try {
            double totalCommissionAndFrais = caisseService.calculateTotalCommissionAndFrais(userId);
            return ResponseEntity.ok(totalCommissionAndFrais);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/list-pages-by-commande/{id}")
    public ResponseEntity<List<String>> getListPageParCommande(@PathVariable String id) {
        List<String> titles = caisseService.getListPageParCommande(id);
        return new ResponseEntity<>(titles, HttpStatus.OK);
    }
    //ADMIN ------------------------------------------------------------------------------------------------------------
    @GetMapping("/getAll")
    public List<Caisse> getList() {
        return caisseRepository.findAll();
    }
    //modifier le solde et compteur la somme de solde ajouter pour le sous admin pour le sous admin
    @PutMapping("/UpdateSoldeSousAdmin/{id}/{id2}")
    public void SoldesousAdmin(@PathVariable String id ,@PathVariable String id2,@RequestBody int i){
        caisseService.UpdateSoldeSousAdmin(id,id2,i);
    }
    @PutMapping("/PagesSoldeSousAdmin/{id}/{id2}")
    public void PagesSoldesousAdmin(@PathVariable String id ,@PathVariable String id2,@RequestBody int i){
        caisseService.PageUpdateSolde(id,id2,i);
    }
    //reset le compteur pour le sous admin
    @PutMapping("/Reset/{id}")
    public void Reset(@PathVariable String id ){
        caisseService.Reset(id);
    }
    @PutMapping("/Etat/{id}")
    public void Update(@PathVariable String id ){
        caisseService.UpdateEtat(id);
    }
    @GetMapping("/todaySales")
    public int todaySles(){
        return caisseService.todaysales();
    }
    // for admin
    @GetMapping("/totalSales")
    public Double totalSales(){
        return caisseService.totalsales();
    }
    //todayAdmin revenu
    @GetMapping("/AdminRevenu")
    public Double AdminRe(){
        return caisseService.adminRevenu();
    }
    //total revenu
    @GetMapping("/AdminTotalRevenu")
    public Double AdminRevenuetotal(){return caisseService.AdmeinRedvenuTotal();}


}

