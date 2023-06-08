package com.example.AppEcommerce.Impl;

import com.example.AppEcommerce.Dto.ArticleDto;
import com.example.AppEcommerce.Enum.Activity;
import com.example.AppEcommerce.Model.Article;
import com.example.AppEcommerce.Model.Pages;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArticleServiceImpl {
    String  addArticle(String idPage, ArticleDto articleDto);

    ResponseEntity<?> addImageToArticle(String id, MultipartFile file)throws IOException;

    List<Article> findByPage(String id);

    ResponseEntity<?> editimage(String id, MultipartFile file)throws IOException;



    ResponseEntity<?> editArticle(ArticleDto articleDto);

    void deleteArticle(String id);







    List<Article> findByCategory(Activity activity);

   /* List<Article> findLocal(Activity activity, double lat1, double long1);*/

    Article getArticleById(String id);

    List<Pages> findLocalPage(Activity activity, double lat1, double long1);
}
