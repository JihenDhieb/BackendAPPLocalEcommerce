package com.example.AppEcommerce.Repository;

import com.example.AppEcommerce.Model.Article;
import com.example.AppEcommerce.Model.ArticleCaisse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleCaisseRepository extends MongoRepository<ArticleCaisse,String> {
}
