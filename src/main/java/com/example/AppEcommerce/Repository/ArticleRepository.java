package com.example.AppEcommerce.Repository;

import com.example.AppEcommerce.Model.Article;
import com.example.AppEcommerce.Model.ArticleCaisse;
import com.example.AppEcommerce.Model.Pages;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends MongoRepository<Article,String> {

   List<Article> findByPage(Pages page);

    List<Article> findAllById(List<ArticleCaisse> articles);
}
