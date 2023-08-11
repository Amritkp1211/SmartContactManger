package com.smartcontact.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smartcontact.Entities.Contact;
import com.smartcontact.Entities.User;
import com.smartcontact.Repository.ContactRepository;
import com.smartcontact.Repository.UserRepository;

@RestController
public class Searchcontroller {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	//Search handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?> Search(@PathVariable("query") String query, Principal principal) {
 
		System.out.println("Query :"+query);
		
		 User user = userRepository.getUserByUserName(principal.getName());
		 
		 List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query, user);
		
		return ResponseEntity.ok(contacts);
	}
}
