package com.example.AppEcommerce.Repository;


import com.example.AppEcommerce.Model.Caisse;
import com.example.AppEcommerce.Model.RevenueDate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RevenueDateRepository extends MongoRepository<RevenueDate,String> {

}
