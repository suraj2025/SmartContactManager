package com.smart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.repo.ContactRepository;
import com.smart.repo.UserRepository;

@RestController
public class SerachController {
	@Autowired
	UserRepository userRepo;
	@Autowired
	ContactRepository contactRepo;
	@GetMapping("/allContact")
     public List<Contact> getAllContact(Principal p){
		String email = p.getName();
		User user = userRepo.findByEmail(email);
		
		List<Contact> contacts = contactRepo.findByUser_Uid(user.getUid());
		return contacts;
    	 
     }
}
