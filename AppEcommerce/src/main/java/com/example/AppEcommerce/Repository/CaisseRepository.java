package com.example.AppEcommerce.Repository;


import com.example.AppEcommerce.Model.Caisse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CaisseRepository extends MongoRepository<Caisse,String> {
    List<Caisse> findByidDelivery(String idDelivery);
    List<Caisse> findByidSender(String idSender);
    List<Caisse> findByidVendor(String idVendor);



}
