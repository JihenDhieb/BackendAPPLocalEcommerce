package com.example.AppEcommerce.Impl;

import com.example.AppEcommerce.Dto.*;
import com.example.AppEcommerce.Model.BenifitsVendor;
import com.example.AppEcommerce.Model.Pages;
import com.example.AppEcommerce.Model.RevenueDate;
import com.example.AppEcommerce.Model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserServiceImpl   {

    User getUserById(String id);

    String  editUser(SignUpUser signUpUser);

    List<Pages> getPagesByUser(String id);

  String getPhoneByUser(String id);



    void editPhone(String  phone, String id);

    User getUserByPage(String id);

    void modifyStatusDelivery(String id);

    String editDelivery(SignUpDelivery signUpDelivery);

    String editLongLatDelivery(editLongLatDelivery longLatDelivery);


  double SetSoldDelivery(double Commission, SignUpDelivery signUpDelivery);

  String editLongLatUser(editLongLatUser longLatUser);

  ResponseEntity<?> addImagesToUser(String id, MultipartFile fileProfile)throws IOException;

    RevenueDate todayRevenue(String id);

    List<RevenueDate> weekRevenue(String id);

    List<RevenueDate> monthRevenue(String id);


    BenifitsVendor todayRevenueVendor(String id);

    List<BenifitsVendor> weekRevenueVendor(String id);

    List<BenifitsVendor> monthRevenueVendor(String id);
}
