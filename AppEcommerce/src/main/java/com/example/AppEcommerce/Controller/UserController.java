package com.example.AppEcommerce.Controller;

import com.example.AppEcommerce.Dto.SignUpDelivery;
import com.example.AppEcommerce.Dto.SignUpUser;
import com.example.AppEcommerce.Dto.editLongLatDelivery;
import com.example.AppEcommerce.Enum.Role;
import com.example.AppEcommerce.Model.BenifitsVendor;
import com.example.AppEcommerce.Model.Pages;
import com.example.AppEcommerce.Model.RevenueDate;
import com.example.AppEcommerce.Model.User;
import com.example.AppEcommerce.Repository.UserRepository;
import com.example.AppEcommerce.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/User")

public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;


    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") String id) {
        return userService.getUserById(id);

    }
    @PutMapping("/edit")
    public String  edit(@RequestBody SignUpUser signUpUser) {
        return userService.editUser(signUpUser);

    }
    @GetMapping("/pagesByUser/{id}")
    public List<Pages> getPagesByUser(@PathVariable("id") String id) {
        return userService.getPagesByUser(id);

    }
    @GetMapping("/Phone/{id}")
    public String  getPhone(@PathVariable("id") String id) {
        return userService.getPhoneByUser(id);

    }
    @PostMapping("/editPhone/{id}")
    public void editPhone( @PathVariable("id") String id ,@RequestBody String  phone ) {
      userService.editPhone(phone,id);
    }
    @GetMapping("/modifyStatusDelivery/{id}")
    public void modifyStatusDelivery(@PathVariable("id") String id) {
       userService.modifyStatusDelivery(id);
    }
    @PutMapping("/editDelivery")
    public String  edit(@RequestBody SignUpDelivery signUpDelivery) {
        return userService.editDelivery(signUpDelivery);
    }
    @PutMapping("/editDeliveryLatLong")
    public String editLongLatDelivery(@RequestBody editLongLatDelivery longLatDelivery) {
        return userService.editLongLatDelivery(longLatDelivery);
    }
    @PostMapping("/addImagesToUser/{id}")
    public ResponseEntity<?> addImagesToUser(@RequestPart(name = "imageProfile", required = false) MultipartFile fileProfile,@PathVariable String id) throws IOException {
        return userService.addImagesToUser(id, fileProfile);
    }
    @GetMapping("/todayRevenue/{id}")
    public RevenueDate todayRevenue(@PathVariable("id") String id) {
        return userService.todayRevenue(id);
    }

    @GetMapping("/weekRevenue/{id}")
    public List<RevenueDate> weekRevenue(@PathVariable("id") String id) {
        return userService.weekRevenue(id);
    }

    @GetMapping("/monthRevenue/{id}")
    public List<RevenueDate> monthRevenue(@PathVariable("id") String id) {
        return userService.monthRevenue(id);
    }
    @GetMapping("/todayRevenuevendor/{id}")
    public BenifitsVendor todayRevenueVendor(@PathVariable("id") String id) {
        return userService.todayRevenueVendor(id);
    }
    @GetMapping("/weekRevenueVendor/{id}")
    public List<BenifitsVendor> weekRevenueVendor(@PathVariable("id") String id) {
        return userService.weekRevenueVendor(id);
    }
    @GetMapping("/monthRevenueVendor/{id}")
    public List<BenifitsVendor> monthRevenueVendor(@PathVariable("id") String id) {
        return userService.monthRevenueVendor(id);
    }
}

