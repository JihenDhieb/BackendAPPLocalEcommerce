package com.example.AppEcommerce.Repository;

import com.example.AppEcommerce.Dto.UserPurchase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPurchaseRepository extends MongoRepository<UserPurchase,String> {
    UserPurchase findUserById(String id);
}
