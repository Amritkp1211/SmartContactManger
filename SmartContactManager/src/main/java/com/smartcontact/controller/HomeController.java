package com.smartcontact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcontact.Entities.User;
import com.smartcontact.Repository.UserRepository;
import com.smartcontact.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	UserRepository userRepository;
	
    //home page hadnlder
	@GetMapping("/")
    public String home(Model model) {
    	model.addAttribute("title","Home-SmartContactManager");
		return "home";
    }
	//about page handler
	@GetMapping("/about")
    public String about(Model model) {
    	model.addAttribute("title","About-SmartContactManager");
		return "about";
    }
	//signup page handler
	@GetMapping("/signup")
    public String Signup(Model model) {
    	model.addAttribute("title","SignUp-SmartContactManager");
    	model.addAttribute("user",new User());
		return "signup";
    }
	
	
//handler for signup/registration	
	 @PostMapping("/do_register")
 	 public String Register(@Valid  @ModelAttribute("user") User user,BindingResult result1, @RequestParam(value = "agreement",defaultValue = "false") boolean agreement,Model model,HttpSession session ){
            try {
            	
            	 if(!agreement) {
              	   System.out.println("do not agreed with term and conditions");
                    throw new Exception("do not agreed with term and conditions");
                }
            	 
            	if (result1.hasErrors()) {
            		System.out.println("Result: "+result1.toString());
            		model.addAttribute("user", user);
            		return "signup";
					
				} 
            	 
            	  user.setRole("ROLE_USER");
                  user.setEnabled(true);
                  user.setImageUrl("user.png");
                  user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                  
                 
                  
                  System.out.println(user);
                  User result = userRepository.save(user);
                  model.addAttribute("user",new User());
                  session.setAttribute("message",new Message("Successfully registerd","alert-success"));
    		      return "signup";
    		
			}
               catch (Exception e) {
			     model.addAttribute("user", user);
			     session.setAttribute("message", new Message("something went wrong!!"+e.getMessage(),"alert-danger"));
			    return "signup";
			}
	}
	
	 @GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("title","Login -SmartContactManager");
		return "login";
	}
	 
	 
}
