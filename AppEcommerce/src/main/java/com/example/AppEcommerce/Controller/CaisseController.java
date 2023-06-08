package com.example.AppEcommerce.Controller;

import com.example.AppEcommerce.Dto.CaisseDto;
import com.example.AppEcommerce.Dto.PushNotificationRequest;
import com.example.AppEcommerce.Dto.getArticleCaisseDto;
import com.example.AppEcommerce.Model.*;
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
        caisseService.SetStatutdelivred(id);
    }
    @GetMapping(value = "/SetSold/{id}")
    public void SetSold(@PathVariable String id) throws FirebaseMessagingException {
        caisseService.SetSold(id);
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
    public int calculateTotalRevenueForCaisse(@PathVariable("id") String caisseId) {
        return caisseService.calculateTotalRevenueForAllCaisse(caisseId);
    }

    @GetMapping("/caisses/{id}/countclient")
    public int countClients(@PathVariable String id) {
        return caisseService.countClients(id);
    }


   @PostMapping("/benefits/{id}")
   public ResponseEntity<String> calculateBenefits(@PathVariable String id) {
       try {
           caisseService.benefitsVendor(id);
           return ResponseEntity.ok("Benefits calculation successful.");
       } catch (NoSuchElementException e) {
           return ResponseEntity.notFound().build();
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during benefits calculation.");
       }
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

}
