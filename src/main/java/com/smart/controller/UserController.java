package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.data.domain.PageRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.smart.Message;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.repo.ContactRepository;
import com.smart.repo.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("user")
public class UserController {
	@Autowired
	UserRepository userRepo;
	@Autowired
	ContactRepository contactRepo;
	@Autowired
	BCryptPasswordEncoder encoder;
	@Autowired
	private Cloudinary cloudinary;

	@ModelAttribute
	public void commonData(Model m, Principal username) {
		User user = userRepo.findByEmail(username.getName());
		m.addAttribute("user", user);
	}

	@GetMapping("/dashboard")
	public String dashboard() {

		return "user/dashboard";
	}

	@GetMapping("/add-contact")
	public String addContact(Model m) {
		m.addAttribute("contact", new Contact());
		return "user/contactForm";
	}

	@PostMapping("/process-contact")
	public String saveContact(@ModelAttribute Contact contact, @RequestParam("profileImg") MultipartFile file,
			Principal username,RedirectAttributes redirectAttributes) {
		

		try {

			if (!file.isEmpty()) {
			    Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
			    String imageUrl = uploadResult.get("secure_url").toString();
			    contact.setImage(imageUrl);  // Store full URL instead of just filename
			} else {
				contact.setImage("https://res.cloudinary.com/deyfwg9o9/image/upload/v1751445610/kpwf20nl6mlrfzkgaap9.jpg");

			}


			User user = userRepo.findByEmail(username.getName());

			if (!contact.getCid().isEmpty()) {
				// Update existing contact

				Optional<Contact> exist = contactRepo.findById(contact.getCid());
				if (exist.isPresent()) {
					System.out.println("contact prasent");
					Contact newContact = exist.get();
					newContact.setName(contact.getName());
					newContact.setEmail(contact.getEmail());
					newContact.setSecondName(contact.getSecondName());
					newContact.setPhone(contact.getPhone());
					newContact.setWork(contact.getWork());
					newContact.setDesc(contact.getDesc());
					if(!file.isEmpty())
					newContact.setImage(contact.getImage());
					contactRepo.save(newContact);
					redirectAttributes.addFlashAttribute("message",new Message("Contact updated successfully","success"));
					
				}
			} else {
//				System.out.println("Creating new");
				contact.setCid(null);
				contact.setUser(user);
				contactRepo.save(contact);
				redirectAttributes.addFlashAttribute("message",new Message("Contact added successfully","success"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			// Optionally add error message attribute
			redirectAttributes.addFlashAttribute("message",new Message("Something went wrong try after some time","danger"));
		}
		return "redirect:/user/view-contacts/0"; // Redirect to avoid resubmission
	}

	
	
	@GetMapping("/view-contacts/{page}")
	public String viewContacts(@PathVariable("page") String page1,
	                           Model m,
	                           Principal principal
	                           ) {
		int page=Integer.parseInt(page1);
	    
	    // Set default page size (you can change this)
	    int pageSize = 5;

	    // Get email of the logged-in user
	    String email = principal.getName();
	    User user = userRepo.findByEmail(email);

	    // Create pageable object
	    Pageable pageable = PageRequest.of(page, pageSize);

	    // Fetch contacts page-wise
	    Page<Contact> contactsPage = contactRepo.findByUser_Uid(user.getUid(), pageable);
	    System.out.println(contactsPage.getContent());

	    m.addAttribute("contacts", contactsPage.getContent());
	    m.addAttribute("currentPage", page);
	    m.addAttribute("totalPages", contactsPage.getTotalPages());
	    
	    

	    return "user/showContact";
	}

	@GetMapping("/delete-contact/{id}")
	public String deleteContact(@PathVariable("id") String id,RedirectAttributes redirectAttributes) {

		contactRepo.deleteById(id);
		redirectAttributes.addFlashAttribute("message",new Message("Contact Deleted successfully","success"));

		return "redirect:/user/view-contacts/0";
	}

	@GetMapping("/update-contact/{id}")
	public String updateContact(@PathVariable("id") String id, Model m) {

		Optional<Contact> contact = contactRepo.findById(id);
		Contact c = contact.get();
		System.out.println(c);

		m.addAttribute("contact", c);
		return "user/contactForm";
	}
	
	@GetMapping("/profile")
	public String profile(Model m) {
		return "user/profile";
	}
	
	@PostMapping("/update-profile-pic")
	public String upadteProfileImage(@RequestParam("profileImage") MultipartFile file,Principal p,RedirectAttributes redirectAttributes) {
		User user = userRepo.findByEmail(p.getName());
		System.out.println(file.getOriginalFilename());
		try {
			
			if (!file.isEmpty()) {
			    Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
			    String imageUrl = uploadResult.get("secure_url").toString();
			    user.setImageUrl(imageUrl);  // Save full URL
			    userRepo.save(user);
			    redirectAttributes.addFlashAttribute("message", new Message("Profile Image updated successfully", "success"));
			}

		} catch (Exception e) {
			// TODO: handle exception
			redirectAttributes.addFlashAttribute("message",new Message("Something went wrong !!!","danger"));
		}
		
		return "redirect:/user/profile";
		
	}
	
	
	@GetMapping("/settings")
	public String settings() {
		return "/user/settings";
	}
	
	@PostMapping("/changePassword")
	public String changePassword(@RequestParam("oldPassword") String oldPwd,
	                             @RequestParam("newPassword") String newPwd,
	                             Principal p, RedirectAttributes redirectAttributes) {
	    User user = userRepo.findByEmail(p.getName());

	    if (encoder.matches(oldPwd, user.getPassword())) {
	        user.setPassword(encoder.encode(newPwd));
	        userRepo.save(user);
	        redirectAttributes.addFlashAttribute("message", new Message("Password changed successfully!", "success"));
	    } else {
	    	redirectAttributes.addFlashAttribute("message", new Message("Incorrect old password.", "danger"));
	    	 return "redirect:/user/settings";
	        // You may want to add flash attributes or error message here
	    }

	    return "redirect:/user/dashboard";
	}

	
	

}
