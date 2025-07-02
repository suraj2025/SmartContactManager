package com.smart.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

//import org.springframework.data.mongodb.repository.MongoRepository;
import com.smart.entities.User;

public interface UserRepository extends MongoRepository<User, String> {
    // You can also define custom query methods here
	User findByEmail(String email);
    
}
