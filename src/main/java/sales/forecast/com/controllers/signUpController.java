package sales.forecast.com.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sales.forecast.com.auth.model.User;

@Controller
@RequestMapping
public class signUpController {
	
	@GetMapping("/register")
	public String signUpForm(User user) {
		return "register";
	}
	@PostMapping("/userdata")
	public String getUserDetailsFromWeb( @ModelAttribute User user, BindingResult result,Model model) {
		// Add Error checking and the storing the user information in DB
		model.addAttribute("user", user);
		return "index";
				
	}

}
