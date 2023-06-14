package com.example.AppEcommerce.Controller;


import com.example.AppEcommerce.Dto.ArticleDto;
import com.example.AppEcommerce.Dto.PagesDto;
import com.example.AppEcommerce.Enum.Activity;
import com.example.AppEcommerce.Model.Article;
import com.example.AppEcommerce.Model.Pages;
import com.example.AppEcommerce.Service.ArticleService;
import com.example.AppEcommerce.Service.PagesService;
import com.google.api.Page;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping(value = "/addArticle/{id}")
    public String addArticle(@PathVariable String id, @RequestBody ArticleDto articleDto) {
        return articleService.addArticle(id, articleDto);
    }

    @PostMapping("/addImageToArticle/{id}")
    public ResponseEntity<?> addImageToArticle(@RequestPart(name = "image", required = false) MultipartFile file, @PathVariable String id) throws IOException {
        return articleService.addImageToArticle(id, file);
    }

    @GetMapping("/findArticlesByPage/{id}")
    public List<Article> findByPage(@PathVariable String id) {
        return articleService.findByPage(id);
    }

    @PostMapping("/editimage/{id}")
    public ResponseEntity<?> editimage(@RequestPart(name = "image", required = false) MultipartFile file, @PathVariable String id) throws IOException {
        return articleService.editimage(id, file);
    }

    @PutMapping("/editArticle")
    public ResponseEntity<?> editArticle(@RequestBody ArticleDto ArticleDto) {
        return articleService.editArticle(ArticleDto);

    }

    @GetMapping(value = "/deleteArticle/{id}")
    public void deleteArticle(@PathVariable String id) {
        articleService.deleteArticle(id);
    }

    @PostMapping("/articlesByCategory")
    public List<Article> articlesByCategory(@RequestBody Activity activity) {
        return articleService.findByCategory(activity);
    }

    @PostMapping("/articlesLocal/{lat}/{lon}")
    public List<Pages> check(@RequestBody Activity activity, @PathVariable double lat, @PathVariable double lon) {
        return articleService.findLocalPage(activity, lat, lon);
    }

    @GetMapping(value = "/getarticle/{id}")
    public Article getArticleById(@PathVariable String id) {
        return articleService.getArticleById(id);
    }


    @PostMapping("/articlesLocalREAUSTAURANTS/{lat}/{lon}")
    public List<Pages> checkReaustaurants(@RequestBody Activity activity, @PathVariable double lat, @PathVariable double lon) {
        return articleService.findLocalPagREAUSTAURANTS(activity, lat, lon);
    }
    @PostMapping("/articlesLocalSUPERETTE/{lat}/{lon}")
    public List<Pages> checkSuperette(@RequestBody Activity activity, @PathVariable double lat, @PathVariable double lon) {
        return articleService.findLocalPagSUPERETTE(activity, lat, lon);
    }
    @PostMapping("/articlesLocalPATISSERIE/{lat}/{lon}")
    public List<Pages> checkPattiserie(@RequestBody Activity activity, @PathVariable double lat, @PathVariable double lon) {
        return articleService.findLocalPatisserie(activity, lat, lon);
    }
}