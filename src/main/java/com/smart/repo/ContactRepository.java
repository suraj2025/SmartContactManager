package com.smart.repo;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.smart.entities.Contact;

@Repository
public interface ContactRepository extends MongoRepository<Contact, String>{

	List<Contact> findByUser_Uid(String uid);

	Page<Contact> findByUser_Uid(String uid, Pageable pageable); // Not findByUserId


}
