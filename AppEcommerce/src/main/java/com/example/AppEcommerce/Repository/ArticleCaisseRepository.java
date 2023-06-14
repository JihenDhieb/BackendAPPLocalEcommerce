package com.example.AppEcommerce.Repository;

import com.example.AppEcommerce.Model.Article;
import com.example.AppEcommerce.Model.ArticleCaisse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArticleCaisseRepository extends MongoRepository<ArticleCaisse,String> {

}
