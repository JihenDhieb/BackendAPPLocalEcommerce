package com.example.AppEcommerce.Service;

import com.example.AppEcommerce.Dto.*;
import com.example.AppEcommerce.Enum.Role;
import com.example.AppEcommerce.Impl.UserServiceImpl;
import com.example.AppEcommerce.Model.*;
import com.example.AppEcommerce.Repository.CaisseRepository;
import com.example.AppEcommerce.Repository.FileRepository;
import com.example.AppEcommerce.Repository.UserRepository;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService implements UserServiceImpl {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    FileRepository fileRepository;
    @Autowired
    CaisseRepository caisseRepository;
    private final WebClient webClient;
    private final String API_KEY = "AIzaSyD_uf7gTZpicdgs_l_dSxWnJ9kZJnUw8jc";
    private final String API_URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private  RestTemplate restTemplate;

    public UserService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://maps.googleapis.com/maps/api/geocode/json")
                .build();
    }


    @Override
    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id " + id));
    }
    @Override
    public String editUser(SignUpUser signUpUser){
        User user = getUserById(signUpUser.getId());
        user.setEmail(signUpUser.getEmail());
        if(!signUpUser.getPassword().equals("laisser vide pour garder le dernier mot de passe")){
            user.setPassword(encoder.encode(signUpUser.getPassword()));
        }
        else{
            user.setPassword(user.getPassword());
        }

        user.setFirstName(signUpUser.getFirstName());
        user.setLastName(signUpUser.getLastName());
        user.setPhone(signUpUser.getPhone());
        User user1 =userRepository.save(user);
        return user1.getId();


    }
    @Override
    public List<Pages> getPagesByUser(String id)
    {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("user not found with ID"+id));
        return user.getPages();

    }
    @Override
    public String getPhoneByUser(String id)
    {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("user not found with ID"+id));
        return user.getPhone();

    }
    @Override
    public void editPhone(String phone, String id){
      User user  = userRepository.findById(id) .orElseThrow(()-> new NoSuchElementException("user not found with ID"+id));
      user.setPhone(phone);
      userRepository.save(user);

    }
  @Override
    public User getUserByPage(String id){
      final User[] userr = {null};
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
           user.getPages().forEach(page -> {
               if(page.getId().equals(id)){
                  userr[0] = user;
               }
           });
        });
        return userr[0];
  }
    @Override
    public void modifyStatusDelivery(String id){
        User user  = userRepository.findById(id) .orElseThrow(()-> new NoSuchElementException("user not found with ID"+id));
       user.setEnLigne(!user.isEnLigne());
       userRepository.save(user);
    }

    @Override
    public String editDelivery(SignUpDelivery signUpDelivery){
        User user = getUserById(signUpDelivery.getId());
        user.setEmail(signUpDelivery.getEmail());
        if(!signUpDelivery.getPassword().equals("laisser vide pour garder le dernier mot de passe")){
            user.setPassword(encoder.encode(signUpDelivery.getPassword()));
        }
        else{
            user.setPassword(user.getPassword());
        }
        user.setFirstName(signUpDelivery.getFirstName());
        user.setLastName(signUpDelivery.getLastName());
        user.setPhone(signUpDelivery.getPhone());
        User user1 =userRepository.save(user);
        return user1.getId();
    }
    @Override
    public String editLongLatDelivery(editLongLatDelivery longLatDelivery){
        User user = getUserById(longLatDelivery.getIdDelivery());
        user.setLatitude(longLatDelivery.getLatitude());
        user.setLongitude(longLatDelivery.getLongitude());
        User userUpdated = userRepository.save(user);
        return userUpdated.getId();
    }

    @Override
    public String editLongLatUser(editLongLatUser longLatUser){
        User user = getUserById(longLatUser.getId());
        user.setLatitude(longLatUser.getLatitude());
        user.setLongitude(longLatUser.getLongitude());
        User userUpdated = userRepository.save(user);
        return userUpdated.getId();
    }
    @Override
    public ResponseEntity<?> addImagesToUser(String id, MultipartFile fileProfile)throws IOException {
        File imageProfile = new File(fileProfile.getOriginalFilename(), fileProfile.getContentType(), fileProfile.getBytes());
        Optional<User> user= userRepository.findById(id);
        fileRepository.save(imageProfile);

        user.get().setImageProfile(imageProfile);
        userRepository.save(user.get());
        return ResponseEntity.ok(new MessageResponse("images registred succssefuly to user "));
    }
    @Override
    public RevenueDate todayRevenue(String id) {
        User user = getUserById(id);
        RevenueDate todayRevenue = null;
        List<RevenueDate> revenues = user.getRevenueDates();
        for (RevenueDate revenueDate : revenues) {
            if (revenueDate.getDate().equals(LocalDate.now())) {
                todayRevenue = revenueDate;
            }
        }
        return todayRevenue;
    }
    @Override
    public List<RevenueDate> weekRevenue(String id) {
        User user = getUserById(id);
        List<RevenueDate> weekRevenue = new ArrayList<>();
        List<RevenueDate> revenues = user.getRevenueDates();
        LocalDate currentDate = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            LocalDate targetDate = currentDate.minusDays(i);
            for (RevenueDate revenueDate : revenues) {
                if (revenueDate.getDate().equals(targetDate)) {
                    weekRevenue.add(revenueDate);
                    break;
                    // Found the revenue for the target date, no need to continue searching
                }
            }
        }

        return weekRevenue;
    }
    @Override
    public List<RevenueDate> monthRevenue(String id) {
        User user = getUserById(id);
        List<RevenueDate> monthRevenue = new ArrayList<>();
        List<RevenueDate> revenues = user.getRevenueDates();
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();

        for (RevenueDate revenueDate : revenues) {
            if (revenueDate.getDate().getMonthValue() == currentMonth) {
                monthRevenue.add(revenueDate);
            }
        }

        return monthRevenue;
    }
    @Override
    public BenifitsVendor todayRevenueVendor(String id) {
        User user = getUserById(id);
        BenifitsVendor todayBenifitsVendor = null;
        List<BenifitsVendor> benifitsVendors = user.getBenifitsVendors();
        for (BenifitsVendor benifitsVendor : benifitsVendors) {
            if (benifitsVendor.getDate().equals(LocalDate.now())) {
                todayBenifitsVendor = benifitsVendor;
                break;
            }
        }
        return todayBenifitsVendor;
    }
    @Override
    public List<User> Livreurs() {
        List<User> u=userRepository.findAll();
        List<User> livreur=new ArrayList<>();
        for(User uu:u){
            if (uu.getRole()== Role.DELIVERY){
                livreur.add(uu);

            }
        }

        return livreur;

    }
    @Override
    public List<User> AdminUsers() {
        List<User> u=userRepository.findAll();
        List<User> admin=new ArrayList<>();
        for(User uu:u){
            if (uu.getRole()== Role.ADMIN){
                admin.add(uu);
            }
        }
        return admin;
    }
    @Override
    public List<BenifitsVendor> weekRevenueVendor(String id) {
        User user = getUserById(id);
        List<BenifitsVendor> weekRevenue = new ArrayList<>();
        List<BenifitsVendor> benifitsVendors = user.getBenifitsVendors();
        LocalDate currentDate = LocalDate.now();

        for (int i = 0; i < 7; i++) {
            LocalDate targetDate = currentDate.minusDays(i);

            for (BenifitsVendor benifitsVendor : benifitsVendors) {
                if (benifitsVendor.getDate().equals(targetDate)) {
                    weekRevenue.add(benifitsVendor);
                    break; // Found the revenue for the target date, no need to continue searching
                }
            }
        }

        return weekRevenue;
    }
    @Override
    public List<BenifitsVendor> monthRevenueVendor(String id) {
        User user = getUserById(id);
        List<BenifitsVendor> monthRevenue = new ArrayList<>();
        List<BenifitsVendor> benifitsVendors = user.getBenifitsVendors();
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();

        for (BenifitsVendor benifitsVendor : benifitsVendors) {
            if (benifitsVendor.getDate().getMonthValue() == currentMonth) {
                monthRevenue.add(benifitsVendor);
            }
        }

        return monthRevenue;
    }


//Admin Jihen
    //Nombre de all Users
@Override
public int CountUsers() {
    List<User> users = userRepository.findAll();
    int count = 0;

    if (users != null) {
        count = users.size();
    }

    return count;
}
//Nombre des AllClient
@Override
public int CountClient() {
    List<User> users = userRepository.findAll();
    int count =0;
    for(User user : users){
        if (user.getRole() == Role.CLIENT) {
            count++;
        }

    }
    return count;
}
//Nombre des AllDelivery
@Override
public int CountDELIVERY() {
    List<User> users = userRepository.findAll();
    int count =0;
    for(User user : users){
        if (user.getRole() == Role.DELIVERY) {
            count++;
        }
    }
    return count;
}
    //Nombre des AllDelivery
    @Override
    public int CountSousAdmin() {
        List<User> users = userRepository.findAll();
        int count =0;
        for(User user : users){
            if (user.getRole() == Role.SOUS_ADMIN) {
                count++;
            }
        }
        return count;
    }
}