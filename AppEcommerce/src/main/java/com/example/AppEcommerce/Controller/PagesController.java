package com.example.AppEcommerce.Controller;

import com.example.AppEcommerce.Dto.PagesDto;
import com.example.AppEcommerce.Dto.SignUpUser;
import com.example.AppEcommerce.Model.Pages;
import com.example.AppEcommerce.Service.PagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/pages")
public class PagesController {
    @Autowired
    private PagesService pagesService;

    @PostMapping(value = "/add/{id}")
    public String addPage(@PathVariable String id, @RequestBody PagesDto pagesDto) {
        return pagesService.addPage(id, pagesDto);
    }

    @PostMapping("/addImagesToPage/{id}")
    public ResponseEntity<?> addImagesToPage(@RequestPart(name = "imageProfile", required = false) MultipartFile fileProfile, @RequestPart(name = "imageCouverture", required = false) MultipartFile fileCouverture, @PathVariable String id) throws IOException {
        return pagesService.addImagesToPage(id, fileProfile, fileCouverture);
    }

    @GetMapping(value = "/getpage/{id}")
    public Pages getPageById(@PathVariable String id) {
        return pagesService.getPagesById(id);
    }

    @PutMapping("/editPage/{id}")
    public String editPage(@PathVariable String id,@RequestBody PagesDto pagesDto) {
        return pagesService.editPage(id,pagesDto);

    }
    @GetMapping(value = "/delete/{idPage}/{idUser}")
    public void deletePage(@PathVariable String idPage, @PathVariable String idUser) {
      pagesService.deletePage(idPage,idUser);
    }
    @PostMapping("/editProfile/{id}")
    public ResponseEntity<?> editPhotoProfile(@RequestPart(name = "imageProfile", required = false) MultipartFile fileProfile, @PathVariable String id) throws IOException {
        return pagesService.editPhotoProfile(id, fileProfile);
    }
    @PostMapping("/editCouverture/{id}")
    public ResponseEntity<?> editCouverture(@RequestPart(name = "imageCouverture", required = false) MultipartFile fileCouverture, @PathVariable String id) throws IOException {
        return pagesService.editPhotoCouverture(id,fileCouverture);
    }
    @GetMapping("/modifyStatusPage/{id}")
    public void modifyStatusPage(@PathVariable("id") String id) {
        pagesService.modifyStatusPage(id);
    }
    @GetMapping("/search")
    public List<String> searchPageByTitle(@RequestParam("letter") String searchLetter) {
        return pagesService.searchPageByTitle(searchLetter);
    }
}
