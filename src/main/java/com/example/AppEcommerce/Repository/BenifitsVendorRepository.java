package com.example.AppEcommerce.Repository;

import com.example.AppEcommerce.Model.BenifitsVendor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenifitsVendorRepository extends MongoRepository<BenifitsVendor,String> {


}
