package com.example.AppEcommerce.Service;

import com.example.AppEcommerce.Dto.MessageResponse;
import com.example.AppEcommerce.Dto.PagesDto;
import com.example.AppEcommerce.Impl.PagesServiceImpl;
import com.example.AppEcommerce.Model.*;
import com.example.AppEcommerce.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class PagesService  implements PagesServiceImpl {
    @Autowired
   private  PagesRepository pagesRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleRepository articleRepository;

    @Override
    public String  addPage(String idUser, PagesDto pagesDto) {
        User user = userRepository.findById(idUser).orElseThrow(null);
        Pages pages  =new Pages(pagesDto.getTitle(),pagesDto.getAddress(),pagesDto.getEmail(),pagesDto.getPhone(),pagesDto.getPostalCode(),pagesDto.getActivity(),pagesDto.getRegion(),pagesDto.getLongitude(),pagesDto.getLatitude(),false,false);
        pagesRepository.save(pages);
        user.getPages().add(pages);
        userRepository.save(user);
        return pages.getId();
    }
    @Override
    public ResponseEntity<?> addImagesToPage(String id, MultipartFile fileProfile, MultipartFile fileCouverture)throws IOException {
        File imageProfile = new File(fileProfile.getOriginalFilename(), fileProfile.getContentType(), fileProfile.getBytes());
        File imageCouverture = new File(fileCouverture.getOriginalFilename(), fileCouverture.getContentType(), fileCouverture.getBytes());
        Optional<Pages> page = pagesRepository.findById(id);
        fileRepository.save(imageProfile);
        fileRepository.save(imageCouverture);
        page.get().setImageProfile(imageProfile);
        page.get().setImageCouverture(imageCouverture);
        pagesRepository.save(page.get());
        return ResponseEntity.ok(new MessageResponse("images registred succssefuly to page "));
    }
    @Override
    public Pages getPagesById(String id) {
        return pagesRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Page not found with id " + id));
    }
    @Override
    public String editPage(String id, PagesDto pagesDto) {
        User user = userRepository.findById(id).orElseThrow(null);
        Pages pages = getPagesById(pagesDto.getId());
        List<Article> articles = articleService.findByPage(pages.getId());
        pages.setTitle(pagesDto.getTitle());
        pages.setAddress(pagesDto.getAddress());

        pages.setPhone(pagesDto.getPhone());
        pages.setPostalCode(pagesDto.getPostalCode());
        pages.setEmail(pagesDto.getEmail());
        pages.setActivity(pagesDto.getActivity());
        pages.setRegion(pagesDto.getRegion());
        pages.setLongitude(pagesDto.getLongitude());
        pages.setLatitude(pagesDto.getLatitude());
        Pages pages1 = pagesRepository.save(pages);
        articles.forEach(article -> {
            article.setPage(pages1);
            articleRepository.save(article);
        });
        user.getPages().forEach(pages2 -> {
            if(pages2.getId().equals(pages1.getId())){
              pages2.setEmail(pages1.getEmail());
              pages2.setAddress(pages1.getAddress());
              pages2.setPhone(pages1.getPhone());
              pages2.setTitle(pages1.getTitle());
              pages2.setPostalCode(pages1.getPostalCode());
              pages2.setActivity(pages1.getActivity());
              pages2.setRegion(pages1.getRegion());
              pages2.setLongitude(pages1.getLongitude());
              pages2.setLatitude(pages1.getLatitude());
              userRepository.save(user);
            }
        });

        return pages1.getId();
    }

    @Override
    public ResponseEntity<?> editPhotoProfile(String id, MultipartFile fileProfile)throws IOException {
        File imageProfile = new File(fileProfile.getOriginalFilename(), fileProfile.getContentType(), fileProfile.getBytes());
        Pages page = getPagesById(id);
        fileRepository.save(imageProfile);
        page.setImageProfile(imageProfile);
        pagesRepository.save(page);
        return ResponseEntity.ok(new MessageResponse("images Profile edit successufuly "));
    }
    @Override
    public ResponseEntity<?> editPhotoCouverture(String id, MultipartFile fileCouverture)throws IOException {
        File imageCouverture = new File(fileCouverture.getOriginalFilename(), fileCouverture.getContentType(), fileCouverture.getBytes());
        Pages page = getPagesById(id);
        fileRepository.save(imageCouverture);
        page.setImageCouverture(imageCouverture);
        pagesRepository.save(page);
        return ResponseEntity.ok(new MessageResponse("images Couverture edit successufuly "));
    }
    @Override
    public void deletePage(String idPage, String idUser) {
        Optional<User> user = userRepository.findById(idUser);
        Pages page = getPagesById(idPage);
        Iterator<Pages> iterator = user.get().getPages().iterator();
        while(iterator.hasNext()){
            Pages page1 = iterator.next();
            if(page1.getId().equals(page.getId())){
                iterator.remove();
                userRepository.save(user.get());
                pagesRepository.delete(page1);
            }
        }
    }
    @Override
    public void modifyStatusPage(String id) {
        Pages page = pagesRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Page not found with ID " + id));
        List<Article> articles = articleRepository.findByPage(page);
        boolean newStatus = !page.isEnligne();
        page.setEnligne(newStatus);
        pagesRepository.save(page);
        for (Article article : articles) {
            article.setPage(page);
            articleRepository.save(article);// Set the new Page for each Article
        }
    }






    @Override
    public List<String> searchPageByTitle(String searchLetter, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));

        List<Pages> pages = pagesRepository.findAll();
        List<String> matchingTitles = new ArrayList<>();

        for (Pages page : pages) {
            String title = page.getTitle();
            if (title.toLowerCase().contains(searchLetter.toLowerCase())) {
                matchingTitles.add(title);
            }
        }



        if (!matchingTitles.isEmpty()) {


            List<String> historiqueRecherches = user.getHistoriquesRecherche();
            historiqueRecherches.add(searchLetter);
            user.setHistoriquesRecherche(historiqueRecherches);
            userRepository.save(user);
        }

        return matchingTitles;
    }
    @Override
    public void changerEtat(String id){
        Pages page = pagesRepository.findById(id) .orElseThrow(()-> new NoSuchElementException("page not found with ID"+id));
        page.setEtat(true);
        pagesRepository.save(page);

    }
}
