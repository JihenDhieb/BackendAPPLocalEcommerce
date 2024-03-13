package com.example.AppEcommerce.Repository;


import com.example.AppEcommerce.Enum.Role;
import com.example.AppEcommerce.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
 public interface UserRepository extends MongoRepository<User,String> {
  User findUserByEmail(String email);
  User findUserById(String id );
  boolean existsByEmail(String email);
  List<User> findDeliveryByRole(Role role);
  List<User> getAllByRole(String role);
  List<User> getUsersByFirstName(String firstname);
  List<User> getUsersByLastName(String lastname);

  List<User> getUsersByFirstNameAndLastName(String firstname,String lastname);



}
