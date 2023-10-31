package com.example.AppEcommerce.Repository;

import com.example.AppEcommerce.Model.History;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends MongoRepository<History,String> {
}
