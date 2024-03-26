package com.example.AppEcommerce.Model;

import com.example.AppEcommerce.Enum.Role;
import com.example.AppEcommerce.Enum.Status;
import org.springframework.data.annotation.Id;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Caisse {

    @Id
    String id;
    String idSender;
    String address;
    String streetAddress;
    String phone;
    String selectedTime;
    String description;
    String idVendor;
    Double subTotal;
    List<ArticleCaisse> articles;
    String idDelivery;
    LocalDate date;
    private String reference;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Caisse(String idSender,String address,String streetAddress,String phone,String selectedTime,String description,String idVendor,Double subTotal, List<ArticleCaisse> articles, LocalDate date  ){
        this.idSender=idSender;
        this.address=address;
        this.streetAddress=streetAddress;
        this.phone=phone;
        this.selectedTime=selectedTime;
        this.description=description;
        this.idVendor=idVendor;
        this.subTotal=subTotal;

        this.articles = articles;
        this.date = date;
        this.reference = generateReference();
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdVendor() {
        return idVendor;
    }

    public void setIdVendor(String idVendor) {
        this.idVendor = idVendor;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ArticleCaisse> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleCaisse> articles) {
        this.articles = articles;
    }

    public String getIdDelivery() {
        return idDelivery;
    }

    public void setIdDelivery(String idDelivery) {
        this.idDelivery = idDelivery;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
    private String generateReference() {
        // Generate a unique reference with a prefix letter and numeric part
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // Generate the prefix letter
        int randomIndex = random.nextInt(letters.length());
        sb.append(letters.charAt(randomIndex));

        // Generate the numeric part
        int numericPart = random.nextInt(9000) + 1000; // Generate a random 4-digit number
        sb.append("-").append(numericPart);

        return sb.toString();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}




