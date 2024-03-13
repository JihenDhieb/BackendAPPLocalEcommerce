package com.example.AppEcommerce.Repository;

import com.example.AppEcommerce.Model.ArticleCaisse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ArticleCaisseRepository extends MongoRepository<ArticleCaisse,String> {

}
