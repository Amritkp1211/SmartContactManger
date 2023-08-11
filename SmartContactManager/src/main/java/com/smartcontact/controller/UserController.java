package com.smartcontact.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.xml.crypto.Data;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.smartcontact.Entities.Contact;
import com.smartcontact.Entities.OrdersDetails;
import com.smartcontact.Entities.User;
import com.smartcontact.Repository.ContactRepository;
import com.smartcontact.Repository.MyOrderRepository;
import com.smartcontact.Repository.UserRepository;
import com.smartcontact.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
 	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private MyOrderRepository myOrderRepository;

	// comman method for response
	@ModelAttribute
	public void commanDataMethod(Model model, Principal principal) {
		String username = principal.getName();
		System.out.println("Username: " + username);

		// get User by username(username);
		User user = userRepository.getUserByUserName(username);

		System.out.println("User :" + user);
		model.addAttribute("user", user);

	}

	// home dahsboard
	@GetMapping("/index")
	public String dashBoard(Model model, Principal principal) {

		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";
	}

	// handler for add contact
	@GetMapping("/add-contact")
	public String addContactForm(Model model) {

		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());

		return "normal/add_contact";
	}

	// add contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, Principal principal,
			@RequestParam("customImage") MultipartFile file, HttpSession session) {

		try {
			String name = principal.getName();

			User user = userRepository.getUserByUserName(name);
			// processing image file
			if (file.isEmpty()) {
				// if file is empty
				contact.setImage("conatact.png");
				
				System.out.println("file is empty");
			} else {
				// if file has data we add file to folder and name to the database
				contact.setImage(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("image uploaded");
			}

			// bidirectional
			contact.setUser(user);

			user.getContacts().add(contact);

			userRepository.save(user);
			System.out.println("contact added");

			// message print
			session.setAttribute("message", new Message("your contact added successfully", "success"));

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());

			session.setAttribute("message", new Message("Something went wrong!!", "danger"));

			e.printStackTrace();
		}

		return "normal/add_contact";
	}

	// handler for show contacts
	//pagination  PER page= 5per*[n]
	//current page = 0 {PAGE}
	
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page,Model model, Principal principal) {
		model.addAttribute("title", "View-Contacts");

		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);
		
		  //currentPage-page 1
		  //contact per page -7
           Pageable pageable = PageRequest.of(page, 5);
           
           
		
		// contact finding by user id
		Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(),pageable);

		model.addAttribute("contacts", contacts);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages",contacts.getTotalPages());
        
        
		return "normal/show-contacts";
	}
	
	//handler for showing perticular contact details
	@GetMapping("/{cid}/contact")
	public String getContactDetails(@PathVariable("cid") Integer cid,
			                                  Model model,Principal principal) {
		
		  model.addAttribute("title","Contact-Details");
		  
		  //contact details by id
		  Contact contact = contactRepository.findById(cid).get();
		  
		     //user id
		    String name = principal.getName();
		    User user = userRepository.getUserByUserName(name);
		    
		    if(user.getId()==contact.getUser().getId()) {
		  
		  model.addAttribute("contact",contact);
		    }	  
		return "normal/contact_detail" ;
	}
	
	//delete contact
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid,
			      Model model,Principal principal,HttpSession session) {
		
		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);
		
		Contact contact = contactRepository.findById(cid).get();
		
		if(user.getId()==contact.getUser().getId()) {
		    
			//assigment remove contact usinr  /img and contact.getimage
			
			 //to equals method of contact
			   user.getContacts().remove(contact);
			   
			   //it will save the user after deleted remains
			   this.userRepository.save(user);
		
		   session.setAttribute("message", new Message("Contact deleted successfully","success"));
		
		}
		
		return "redirect:/user/show-contacts/0";
	}
	
	//update form open  contact handler
	  @PostMapping("/update-contact/{cid}")
      public String openUpdateContactform(@PathVariable("cid") Integer cid,Model model) {
		  
		  model.addAttribute("title","update contact");
		  
		     Contact contact = contactRepository.findById(cid).get();
		   
		     model.addAttribute("contact", contact);
		     
    	  return "normal/update_form";
      }
	
	//update contact handler
	  @PostMapping("/process-update")
	  public String updateContact(@ModelAttribute Contact contact ,@RequestParam("customImage") MultipartFile file
			                            ,Model m,HttpSession session,Principal principal){
		  
		  try {
			  
			  Contact oldcontact = contactRepository.findById(contact.getCid()).get();
			 
			  //image
			  if(!file.isEmpty()) {
				  
				  //old image delete				  
				  File deletefile = new ClassPathResource("static/img").getFile();
				    
	     		   File file2 = new File(deletefile,oldcontact.getImage());
		 
	     		   file2.delete();
				  
				  //rewrite image
				  //new image upload
				  File savefile = new ClassPathResource("static/img").getFile();
				   
				  
				  Path path = Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				  
				  Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				   
				  contact.setImage(file.getOriginalFilename());
				  
				  System.out.println("image updated successfully");
			  }
			  else{
				    contact.setImage(oldcontact.getImage());
			  }
			  
			   String name = principal.getName();
               User user = userRepository.getUserByUserName(name);	
               
               contact.setUser(user);
               
               contactRepository.save(contact);
               
               session.setAttribute("message",new Message("Your contact  updated successfully", "success"));
               
		} catch (Exception e) {
                   e.printStackTrace();
		}
		  
		  
		  return "redirect:/user/"+contact.getCid()+"/contact";
	  }
	  
	  //your profile
	  @GetMapping("/profile")
	  public String yourProfile(Model model) {

         model.addAttribute("title","User-Profile");
		  //no need of user to get it already done in commanmethod above
          //user is already in each request
         
         return "normal/profile";
	  }
	  
	  //user profile update handler
	  @PostMapping("/update-user")
	  public String updateUser(Principal principal,Model model) {
		  
		  User user = userRepository.getUserByUserName(principal.getName());
		  
		  model.addAttribute("user", user);
		  
		  return "normal/update_user";
	  }
	  
	  //user-update-process
	  @PostMapping("/user-update-process")
	    public String updateUserProcess(Principal principal,@ModelAttribute User user,@RequestParam("customImage") MultipartFile file,HttpSession session,Model model) throws IOException {
		  
		  User old_user= userRepository.getUserByUserName(principal.getName());
		  
		  if(!file.isEmpty()) {
			 
			  //deleting old user is image
			  File deleteFile = new ClassPathResource("/static/img").getFile();
		      File deletFilenow = new File(deleteFile, old_user.getImageUrl());
			  deletFilenow.delete();
			  
			  //save user image 			  
			  File saveFile = new ClassPathResource("static/img").getFile();
			  
			  Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			      
			    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			    
			    user.setImageUrl(file.getOriginalFilename());
			      
		  }
		  else {
			    user.setImageUrl(old_user.getImageUrl());
			    
		  }
		     userRepository.save(user);
		     session.setAttribute("message",new Message("User profile updated successfully logout and login again see changes","success"));
	    	return "redirect:/user/index";
	    }
	  
	  //settings handler
	  @GetMapping("/settings")
	  public String settings(Model model) {
		  
	   model.addAttribute("title","SmartConManager-Settings");
		  
		  return "normal/settings";
	  }
	  
	//change password handler
	  @PostMapping("/change-password")
	  public String changepassword(@RequestParam("oldPassword") String oldPassword
			                                       ,@RequestParam("newPassword") String newPassword,Principal principal,HttpSession session){
          
		  User currentUser= userRepository.getUserByUserName(principal.getName());
		  
		  if(bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
			  
			  //change password
		      currentUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
		      userRepository.save(currentUser);
		      
		      session.setAttribute("message",new Message("Password updated successfully","success"));
		       
		      
		  }
		  else {
			  //error
		      session.setAttribute("message",new Message("Wrong old Password!! enter correct password","danger"));
		      return "redirect:/user/settings";
		  }
		  
		  return "redirect:/user/index";
	  }

	  //Creatin order for payment
	  @ResponseBody
	  @PostMapping("/create_order")
	  public String createOrder(@RequestBody Map<String,Object> data,Principal principal) throws Exception {
		  System.out.println(data);
		  
		int amt = Integer.parseInt(data.get("amount").toString());
		    
		var client = new RazorpayClient("rzp_test_SW3Zn4oCIFMAWU", "yrSX40U23Rzu5d0LU3Sdlv5l");
		
		
		
		JSONObject object=new JSONObject();
		object.put("amount", amt*100);
		object.put("currency", "INR");
		object.put("receipt", "txn-20112012");
		
		//creating new order
		Order order = client.orders.create(object);
		System.out.println("Order: "+order);
		
		//save the order
		OrdersDetails ordersDetails=new OrdersDetails();
		
		
		
		 ordersDetails.setAmount(order.get("amount")+"");
	     ordersDetails.setOrderId(order.get("id"));
	     ordersDetails.setPaymentId(order.get(null));
	     ordersDetails.setStatus("created");
	     ordersDetails.setDate(new java.util.Date());
	     
	     User user = userRepository.getUserByUserName(principal.getName());
	     
	     ordersDetails.setUser(user);
	     ordersDetails.setReceipt(order.get("receipt"));
	     
	     myOrderRepository.save(ordersDetails);
	     
	    return order.toString();
	    
	  }
	  
	  //update order handler
	  @PostMapping("/update-order")
	  public ResponseEntity<?> updateOrder(@RequestBody Map<String,Object> data){
		  
		  OrdersDetails details = myOrderRepository.findByOrderId(data.get("order_id").toString());
		  System.out.println(data);
		  
		  details.setPaymentId(data.get("payment_id").toString());
		  details.setStatus(data.get("status").toString());
		  
		  myOrderRepository.save(details);
		  
		  return ResponseEntity.ok(Map.of("msg","updated"));
	  }

}
