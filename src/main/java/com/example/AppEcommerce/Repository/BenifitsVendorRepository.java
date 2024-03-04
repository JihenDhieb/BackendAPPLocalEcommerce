package com.example.AppEcommerce.Repository;

import com.example.AppEcommerce.Model.BenifitsVendor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BenifitsVendorRepository extends MongoRepository<BenifitsVendor,String> {


}
