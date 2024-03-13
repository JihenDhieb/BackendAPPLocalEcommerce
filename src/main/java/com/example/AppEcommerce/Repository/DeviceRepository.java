package com.example.AppEcommerce.Repository;

import com.example.AppEcommerce.Model.Caisse;
import com.example.AppEcommerce.Model.Device;
import com.example.AppEcommerce.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface DeviceRepository extends MongoRepository<Device,String> {
    Optional<Device> findOneByToken(String token);
    List<Device> findAllByUserIdIn(Set<String> usersIds);
    List<Device> findByUser(User user);
}
