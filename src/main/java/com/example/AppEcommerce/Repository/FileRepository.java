package com.example.AppEcommerce.Repository;

import com.example.AppEcommerce.Model.File;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository  extends MongoRepository<File,String> {
}
