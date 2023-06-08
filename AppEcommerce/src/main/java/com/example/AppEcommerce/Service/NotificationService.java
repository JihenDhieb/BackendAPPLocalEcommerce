package com.example.AppEcommerce.Service;

import com.example.AppEcommerce.Enum.Role;
import com.example.AppEcommerce.Model.*;

import com.example.AppEcommerce.Repository.*;
import com.google.firebase.messaging.*;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    FcmService fcmService;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CaisseRepository caisseRepository;
    @Autowired
    ArticleRepository articleRepository;



    @Autowired
    ArticleService articleService;

    public Device addDevice(String token, String email){
        Optional<Device> optDevice =deviceRepository.findOneByToken(token);
        if(optDevice.isPresent()){
            return null;
        }
        User user = userRepository.findUserByEmail(email);
        if(user.isPresent()) {
            Device device =new Device(token);
            device.setUser(user);
            return deviceRepository.save(device);


        }
        return null;
    }
    public List<Device> devices(String id){
        List<Device> devices = deviceRepository.findAll();
        List<Device> devices1 = new ArrayList<>();
        devices.forEach(device -> {
            if(device.getUser().getId().equals(id)){
                devices1.add(device);
            }
        });
        return devices1;
    }
    public List<Device> findAllByUserIdIn(Set<String> usersIds){
        List<Device> devices = deviceRepository.findAll();
        List<Device> devices1 = new ArrayList<>();
        usersIds.forEach(id ->{
            devices.forEach(device ->{
                if(device.getUser().getId().equals(id)){
                    devices1.add(device);
                }
            });
        });
        return devices1;
    }


    public void sendVendorPushNotification(String idCaisse) throws FirebaseMessagingException {
        Caisse caisse = caisseRepository.findById(idCaisse)
                .orElseThrow(()-> new NoSuchElementException("caisse not found with ID"+ idCaisse));
        User user = userRepository.findById(caisse.getIdVendor())
                .orElseThrow(()-> new NoSuchElementException("user not found with ID"+ caisse.getIdVendor()));
        List<Device> devices = devices(user.getId());
        AtomicReference<String> body = new AtomicReference<>("You have received a new order ");

     Notif notif = new Notif("New Order", body.get(), caisse.getId());
        Notif savedNotif = notificationRepository.save(notif);
        FirebaseMessaging messaging = FirebaseMessaging.getInstance();
        Notification notification = Notification.builder()
                .setTitle(savedNotif.getTitle())
                .setBody(savedNotif.getBody())
                .build();
        Map<String, String> data = new HashMap<>();
        data.put("idCaisse", idCaisse);
        if(devices.size() > 0) {
            devices.forEach(d -> {
                try {

                    Message message = Message.builder()
                            .setNotification(notification)
                            .putAllData(data)
                            .setToken(d.getToken())
                            .build();
                    messaging.send(message);

                } catch (FirebaseMessagingException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void sendDeliveriesPushNotificationLocal(String idCaisse) throws FirebaseMessagingException {
        Caisse caisse = caisseRepository.findById(idCaisse)
                .orElseThrow(()-> new NoSuchElementException("caisse not found with ID"+ idCaisse));
        ArticleCaisse premierArticleCaisse = caisse.getArticles().get(0);
        Article premierArticle = articleRepository.findById(premierArticleCaisse.getIdArticle())
                .orElseThrow(()-> new NoSuchElementException("article not found with ID"+ premierArticleCaisse.getIdArticle()));
        Pages page = premierArticle.getPage();

        List<User> deliveries = userRepository.findDeliveryByRole(Role.DELIVERY);

        List<User> deliveriesWithSold = new ArrayList<>();
        deliveries.forEach(del ->{
            if(del.getSold() > 0 && del.isEnLigne()){
                deliveriesWithSold.add(del);
            }
        });
        double MAX_DISTANCE = 10.0;
        List<User> nearbyDeliveries = deliveriesWithSold.stream()
                .filter(delivery -> articleService.calculate(delivery.getLatitude(), delivery.getLongitude(), page.getLatitude(), page.getLongitude()) <= MAX_DISTANCE)
                .limit(10)
                .collect(Collectors.toList());
        Set<String> deliveriesIds = new HashSet<>();
        nearbyDeliveries.forEach(delivery -> {
            deliveriesIds.add(delivery.getId());
        });
        List<Device> devices =findAllByUserIdIn(deliveriesIds);
        AtomicReference<String> body = new AtomicReference<>("You have received a new delivery ");
        Notif notif = new Notif("New Delivery", body.get(), caisse.getId());
        Notif savedNotif = notificationRepository.save(notif);
        FirebaseMessaging messaging = FirebaseMessaging.getInstance();
        Notification notification = Notification.builder()
                .setTitle(savedNotif.getTitle())
                .setBody(savedNotif.getBody())
                .build();
        Map<String, String> data = new HashMap<>();
        data.put("idCaisse", idCaisse);
        if(devices.size() > 0) {
            devices.forEach(d -> {
                try {
                    Message message = Message.builder()
                            .setNotification(notification)
                            .putAllData(data)
                            .setToken(d.getToken())
                            .build();
                    messaging.send(message);

                } catch (FirebaseMessagingException e) {
                    e.printStackTrace();
                }
            });
        }

    }
    //notification vers vendeur lorsque le livreur accepte la commande
    public void sendNotificationFromDeliveryToVendor(String idCaisse) throws FirebaseMessagingException {
        Caisse caisse = caisseRepository.findById(idCaisse)
                .orElseThrow(()-> new NoSuchElementException("caisse not found with ID"+ idCaisse));
        User user = userRepository.findById(caisse.getIdVendor())
                .orElseThrow(()-> new NoSuchElementException("user not found with ID"+ caisse.getIdVendor()));
        List<Device> devices = devices(user.getId());
        AtomicReference<String> body = new AtomicReference<>("Delivery guy is coming For order: " + caisse.getReference());

        Notif notif = new Notif("Delivery coming", body.get(), caisse.getId());
        Notif savedNotif = notificationRepository.save(notif);
        FirebaseMessaging messaging = FirebaseMessaging.getInstance();
        Notification notification = Notification.builder()
                .setTitle(savedNotif.getTitle())
                .setBody(savedNotif.getBody())
                .build();
        Map<String, String> data = new HashMap<>();
        data.put("idCaisse", idCaisse);
        if(devices.size() > 0) {
            devices.forEach(d -> {
                try {

                    Message message = Message.builder()
                            .setNotification(notification)
                            .putAllData(data)
                            .setToken(d.getToken())
                            .build();
                    messaging.send(message);

                } catch (FirebaseMessagingException e) {
                    e.printStackTrace();
                }
            });
        }
    }
    //notification vers livreur lorsque le vendeur annule la commande
    public void sendCancelNotificationByVendor(String idCaisse) throws FirebaseMessagingException {
        Caisse caisse = caisseRepository.findById(idCaisse)
                .orElseThrow(()-> new NoSuchElementException("caisse not found with ID"+ idCaisse));
        User user = userRepository.findById(caisse.getIdDelivery())
                .orElseThrow(()-> new NoSuchElementException("user not found with ID"+ caisse.getIdDelivery()));
        List<Device> devices = devices(user.getId());

        AtomicReference<String> body = new AtomicReference<>("The order with reference: " + caisse.getReference() + " has being canceled");

        Notif notif = new Notif("Order Cancel", body.get(), caisse.getId());
        Notif savedNotif = notificationRepository.save(notif);
        FirebaseMessaging messaging = FirebaseMessaging.getInstance();
        Notification notification = Notification.builder()
                .setTitle(savedNotif.getTitle())
                .setBody(savedNotif.getBody())
                .build();
        Map<String, String> data = new HashMap<>();
        data.put("idCaisse", idCaisse);
        if(devices.size() > 0) {
            devices.forEach(d -> {

                try {

                    Message message = Message.builder()
                            .setNotification(notification)
                            .putAllData(data)
                            .setToken(d.getToken())
                            .build();
                    messaging.send(message);

                } catch (FirebaseMessagingException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    //notification vers livreur et vendeur  lorsque le client annule la commande
    public void sendCancelNotificationByClient(String idCaisse) throws FirebaseMessagingException {

        Caisse caisse = caisseRepository.findById(idCaisse)
                .orElseThrow(()-> new NoSuchElementException("caisse not found with ID"+ idCaisse));
        User vendor = userRepository.findById(caisse.getIdVendor())
                .orElseThrow(()-> new NoSuchElementException("user not found with ID"+ caisse.getIdVendor()));
        Set<String> usersIds = new HashSet<>();
        AtomicReference<String> body = new AtomicReference<>("The order with reference: " + caisse.getReference() + " has being canceled by client");

        Notif notif = new Notif("Order Cancel By Client", body.get(), caisse.getId());
        Notif savedNotif = notificationRepository.save(notif);
        FirebaseMessaging messaging = FirebaseMessaging.getInstance();
        Notification notification = Notification.builder()
                .setTitle(savedNotif.getTitle())
                .setBody(savedNotif.getBody())
                .build();
        Map<String, String> data = new HashMap<>();
        data.put("idCaisse", idCaisse);
        if (caisse.getIdDelivery() != null) {
            User delivery = userRepository.findById(caisse.getIdDelivery())
                    .orElseThrow(()-> new NoSuchElementException("user not found with ID"+ caisse.getIdDelivery()));
            usersIds.add(delivery.getId());
            usersIds.add(vendor.getId());
            List<Device> devices =findAllByUserIdIn(usersIds);

            if(devices.size() > 0) {
                devices.forEach(d -> {

                    try {

                        Message message = Message.builder()
                                .setNotification(notification)
                                .putAllData(data)
                                .setToken(d.getToken())
                                .build();
                        messaging.send(message);

                    } catch (FirebaseMessagingException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        else{
            usersIds.add(vendor.getId());
            List<Device> devices =findAllByUserIdIn(usersIds);

            if(devices.size() > 0) {
                devices.forEach(d -> {

                    try {

                        Message message = Message.builder()
                                .setNotification(notification)
                                .putAllData(data)
                                .setToken(d.getToken())
                                .build();
                        messaging.send(message);

                    } catch (FirebaseMessagingException e) {
                        e.printStackTrace();
                    }
                });
            }
        }

    }

}
