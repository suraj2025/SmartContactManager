package com.smart.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.repo.ContactRepository;
import com.smart.repo.UserRepository;

@Controller("/")
public class HomeController {
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	@Autowired
	UserRepository userRepo;
	@Autowired
	ContactRepository contactRepo;

	@GetMapping("/")
	public String test() {

		return "Home";
	}

	@GetMapping("/about")
	public String about() {

		return "about";
	}

	@GetMapping("/signup")
	public String signup() {

		return "signup";
	}

	@GetMapping("/login")
	public String login() {

		return "login";
	}

	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute User user) {

		user.setEnabled(true);
		user.setRole("ROLE_USER");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepo.save(user);
		return "login";

	}
	@GetMapping("/demo")
	public String demo(
			Model m) {
		Optional<User> user = userRepo.findById("68467589eab638841ad6c90c");
		System.out.println(user.get());
		
		for(int i=0;i<15;i++) {
			Contact c=new Contact();
			c.setName("name"+(i+1));
			c.setEmail("name"+(i+1)+"@gmail.com");
//			c.setSecondName();
			
			c.setPhone(""+Math.random()*10000000);
			c.setWork("work"+(i+1));
			c.setDesc("desc"+i+1);
			c.setImage("profile.png");
			c.setUser(user.get());
			contactRepo.save(c);
			
		}
		return "demo";
	}

//	@GetMapping("/user/dashboard")
//	public String dashboard() {
//		return "dashboard";
//	}
}
