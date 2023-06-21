package com.example.AppEcommerce.Impl;

import com.example.AppEcommerce.Dto.PagesDto;
import com.example.AppEcommerce.Model.Pages;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PagesServiceImpl {


    String  addPage(String idUser, PagesDto pagesDto);

    ResponseEntity<?> addImagesToPage(String id, MultipartFile fileProfile, MultipartFile fileCouverture)throws IOException;



    Pages getPagesById(String id);



    String editPage(String id, PagesDto pagesDto);



    ResponseEntity<?> editPhotoProfile(String id, MultipartFile fileProfile)throws IOException;

    ResponseEntity<?> editPhotoCouverture(String id, MultipartFile fileCouverture)throws IOException;

    void deletePage(String idPage, String idUser);

    void modifyStatusPage(String id);


    List<String> searchPageByTitle(String searchLetter);
}
