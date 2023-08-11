package com.smartcontact.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcontact.Entities.User;
import com.smartcontact.Repository.UserRepository;
import com.smartcontact.helper.Message;
import com.smartcontact.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotController {

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;
	
	// forgot password handler
	@GetMapping("/forgot")
	public String forgotPassword() {

		return "forgot_password_form";
	}

	// send otp handler
	@PostMapping("/send-otp")
	public String sendOTp(@RequestParam("email") String email,HttpSession session) {

		System.out.println("Email: " + email);

		// Generating random 4 digit otp
		Random random = new Random();

		int otp = random.nextInt(9999);
		System.out.println("OTP: " + otp);

		// write code to verify otp on email
		String subject = "OTP from SCM";
		String message = ""
				     + "<div style='border:1px solid #e2e2e2; padding:20px'>"
				     + "<h1>"
				     + "OTP is : "
				     + "<b>"+otp
				     + "</b>"
				     + "</h1>"
				     + "</div>";
		String to = email;

		boolean flag = emailService.sendEmail(subject, message, to);

		if (flag) {		
			
			session.setAttribute("myotp", otp);
			session.setAttribute("email",email);
			return "verify_otp";
		
		} else 
		{
			session.setAttribute("message","check your email");
			
               return "forgot_password_form";			
		}
	}
	
	//verify otp handler
	@PostMapping("/verify-otp")
	public String verifyOTP(@RequestParam("otp") int otp ,HttpSession session) {
		
		int myOtp=(int) session.getAttribute("myotp");
		String email=(String) session.getAttribute("email");
		
		if(myOtp==otp) {
			//change password form
			  User user = userRepository.getUserByUserName(email);
			
			  if(user==null) {
				  //send error message
				  
				  session.setAttribute("message","User with this email doesn't exits !!");
					
	               return "forgot_password_form";	
			  }
			  else {
				  //send change password form
			  }
			return "change_password";
		}
		else {
			
			session.setAttribute("message","Your Entered wrong OTP");
			
			return "verify_otp";
		}	
	}
	
	//change password handler
	@PostMapping("/change-password")
    public String changePassword(@RequestParam("newpassword") String newpassword,HttpSession session) {
		
		String email=(String) session.getAttribute("email");
		  
		User user = userRepository.getUserByUserName(email);

		user.setPassword(bcryptEncoder.encode(newpassword));
		
		 //checking wheather old password is same as new password
		 if(bcryptEncoder.matches(newpassword, user.getPassword())) {
			session.setAttribute("message","you entered your last password try diff pass..");
				System.out.println("error--------");
			    return "change_password";
		 }
		 
		userRepository.save(user); 
				
		return "redirect:/login?change=password changed successfully";
		
	}
}
