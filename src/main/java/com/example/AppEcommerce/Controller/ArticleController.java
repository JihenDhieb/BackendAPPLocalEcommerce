package com.example.AppEcommerce.Controller;


import com.example.AppEcommerce.Dto.ArticleDto;
import com.example.AppEcommerce.Dto.PagesDto;
import com.example.AppEcommerce.Dto.Products;
import com.example.AppEcommerce.Dto.UserPurchase;
import com.example.AppEcommerce.Enum.Activity;
import com.example.AppEcommerce.Enum.Status;
import com.example.AppEcommerce.Model.*;
import com.example.AppEcommerce.Repository.ArticleRepository;
import com.example.AppEcommerce.Repository.CaisseRepository;
import com.example.AppEcommerce.Repository.UserPurchaseRepository;
import com.example.AppEcommerce.Repository.UserRepository;
import com.example.AppEcommerce.Service.ArticleService;
import com.example.AppEcommerce.Service.CaisseService;
import com.example.AppEcommerce.Service.PagesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.Page;
import jdk.jfr.Category;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;



@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
   private  UserPurchaseRepository userPurchaseRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CaisseRepository caisseRepository;
    @Autowired
    private CaisseService caisseService;
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
    @PostMapping("/articlesLocalCAFE/{lat}/{lon}")
    public List<Pages> checkCAFE(@RequestBody Activity activity, @PathVariable double lat, @PathVariable double lon) {
        return articleService.findLocalCAFE(activity, lat, lon);
    }
    @GetMapping("/pages/{title}")
    public ResponseEntity<Pages> getPageByTitle(@PathVariable String title) {
        try {
            Pages page = articleService.findPagebytitle(title);
            return ResponseEntity.ok(page);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
    public List<Products>getProduct(){
        List<Products> Products=new ArrayList<>();
        List<Article> a=articleRepository.findAll();
        for(Article aa:a){
            Products.add(new Products(aa.getId(),aa.getNom(),aa.getDescription(), aa.getPrix()));
        }
        return Products;
    }

    //get List article recommend by user
    @GetMapping(value = "/ArticleRecommend/{id}")//on change id de userpurchase par id du user directement pour chercher le historique
    public   List<Article> getArticleRecommend(@PathVariable String id){
        RestTemplate restTemplate = new RestTemplate();
        //UserPurchase u=userPurchaseRepository.findUserById(id);
        //List<History> historique =u.getProductId();
        List<Caisse> lc=caisseService.caisseListClient(id);
        List<History> historique=new ArrayList<>();
        for(Caisse c :lc){

            c.getArticles().forEach(aa->{
                History h =new History(aa.getIdArticle());
                if(!(historique.contains(h)))
                    historique.add(h);
            });
        }

        // Définissez les en-têtes de la requête
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        // Créez le corps de la requête
        String requestBody = "{\" Products\": \"Products\",\"history\": \"historique\"}"; // Remplacez avec votre corps de requête réel
        List<Products> pp=getProduct();

        Recommend myData = new Recommend(pp, historique);

        ObjectMapper mapper = new ObjectMapper();
        List<Article> l=new ArrayList<>();

        try {
            String json = mapper.writeValueAsString(myData);

            // Créez l'objet HttpEntity avec les en-têtes et le corps de la requête
            HttpEntity<String> httpEntity = new HttpEntity<>(json, headers);

            // Envoyez la requête POST et obtenez la réponse
            ResponseEntity<String> response = restTemplate.exchange("http://127.0.0.1:8000/recommend_products",
                    HttpMethod.POST, httpEntity, String.class);
            // Traitez la réponse

            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            ResponceBody responseObj = objectMapper.readValue(responseBody, ResponceBody.class);

            List<Article> l2=articleRepository.findAll();
            for(Products r:responseObj.getRecommendedProducts() ){
                for(Article ar:l2){
                    if (ar.getDescription().equals((r.getDescription()))&&ar.getNom().equals((r.getNom()))){
                        l.add(ar);

                    }
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return l;
    }

}