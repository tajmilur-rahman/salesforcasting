package sales.forecast.com.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sales.forecast.com.auth.model.User;

@Controller

public class defaultPage {
	@RequestMapping(value="/defaultpage",method=RequestMethod.POST)
	public String defaultPageLink(@ModelAttribute User user) {
		System.out.println("Default Page:");
		System.out.println("User:"+user.getEmail());
//		System.out.println(model.getAttribute("email"));
		return "defaultpage";
	}
}
