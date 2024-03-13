package com.example.AppEcommerce.Controller;

import com.example.AppEcommerce.Dto.SignUpDelivery;
import com.example.AppEcommerce.Dto.SignUpUser;
import com.example.AppEcommerce.Dto.editLongLatDelivery;
import com.example.AppEcommerce.Dto.editLongLatUser;
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


    @PutMapping("/editUserLatLong")
    public String editLongLatUser(@RequestBody editLongLatUser longLatUser) {
        return userService.editLongLatUser(longLatUser);
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
    }@GetMapping("/monthRevenue/{id}")
    public List<RevenueDate> monthRevenue(@PathVariable("id") String id) {
        return userService.monthRevenue(id);
    }
    @GetMapping("/email/{email}")
    public User getUserByemail(@PathVariable("email") String email) {
        return userRepository.findUserByEmail(email);

    }
    @GetMapping("/listeUser")
    public List<User> getUser() {
        return userRepository.findAll();

    }
    @GetMapping("/Admins")
    public List<User> Admin(){
        return userService.AdminUsers();
    }
    @GetMapping("/Livreurs")
    public List<User> livreur(){
        return userService.Livreurs();
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
    //Admin Jihen*-------------------------------------------------------------------------------------------------------------
    @GetMapping("/count")
    public int countUsers() {
        return userService.CountUsers();
    }
    @GetMapping("/countClient")
    public int countClient() {
        return userService.CountClient();
    }
    @GetMapping("/countDELIVERY")
    public int countDELIVERY() {
        return userService.CountDELIVERY();
    }
    @GetMapping("/countSousAdmin")
    public int countSousAdmin() {
        return userService.CountSousAdmin();
    }

    @GetMapping("/getAllClient")
    public List<User> getAllClients(){
        return userService.getAllClient();
    }
    @GetMapping("/getAllDelivery")
    public List<User> getAlDelivery(){
        return userService.getAllDelivery();
    }
    @GetMapping("/getAllVendeur")
    public List<User> getAllVendeur(){
        return userService.getAllVendeur();
    }
    @GetMapping("/getAllSousAdmin")
    public List<User> getAllSousAdmin(){
        return userService.getAllSousAdmin();
    }

    @GetMapping("/getClientById/{id}")
    public User getClientById(@PathVariable("id")String id){
        return userService.getClientById(id);
    }
    @GetMapping("/getVendeurById/{id}")
    public User getVendeurById(@PathVariable("id")String id){
        return userService.getVendeurById(id);
    }
    @GetMapping("/getDeliveryById/{id}")
    public User getDeliveryById(@PathVariable("id")String id){
        return userService.getDeliveryById(id);
    }
    @GetMapping("/getSousAdminById/{id}")
    public User getSousAdminById(@PathVariable("id")String id){
        return userService.getSousAdminById(id);
    }
    @GetMapping("/getClientByName/{name}")
    public List<User> getClientsByFirstname(@PathVariable("name")String name){
        return userService.getClientsByName(name);
    }
    @GetMapping("/getClientsByLastname/{lastname}")
    public List<User> getClientsByLastname(@PathVariable("lastname")String lastname){
        return userService.getClientsByLastname(lastname);
    }

    @GetMapping("/getClientsByNameAndLastname/{firstname}/{lastname}")
    public List<User> getClientsByNameAndLastname(@PathVariable("firstname")String firstname,@PathVariable("lastname")String lastname){
        return userService.getClientsByNameAndLastname(firstname,lastname);
    }





}

