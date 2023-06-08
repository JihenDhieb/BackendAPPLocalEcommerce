package com.example.AppEcommerce.Repository;


import com.example.AppEcommerce.Model.Caisse;
import com.example.AppEcommerce.Model.RevenueDate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RevenueDateRepository extends MongoRepository<RevenueDate,String> {

}
