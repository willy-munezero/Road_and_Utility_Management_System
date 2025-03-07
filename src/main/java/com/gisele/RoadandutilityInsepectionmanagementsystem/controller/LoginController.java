package com.gisele.RoadandutilityInsepectionmanagementsystem.controller;


import com.gisele.RoadandutilityInsepectionmanagementsystem.Domain.Role;
import com.gisele.RoadandutilityInsepectionmanagementsystem.Domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class LoginController {

	private Logger logger = Logger.getLogger(getClass().getName());

	@GetMapping("/showMyLoginPage")
	public String showMyLoginPage(HttpSession session) {
//		session.invalidate();
		return "Authentication/login";
		
	}

	@GetMapping("/showForgotPassword")
	public String showForgotPassword() {
		return "Authentication/forgotPassword";
	}

	@GetMapping("/showResetPassword")
	public String showResetPassword(HttpSession session, Model model) {
		Long UserId =  (Long)session.getAttribute("resetId");
		User existing =  (User)session.getAttribute("existing");
		List<Role> roles =  (List<Role>)session.getAttribute("Reset_roles");
		String formRole = "";
		if(UserId!=null){
			logger.info("===================================================================================");
			for(Role role:roles){
				formRole = role.getName();
			}
			logger.info("Form Role: " + formRole);
			existing.setFormRole(formRole);
			logger.info(existing.toString());
			model.addAttribute("existing", existing);
			model.addAttribute("resetId", UserId);
			logger.info("Session Resetting user : "+UserId);
		}else{
			logger.info("Session reset target user not found: ");
		}
		return "Authentication/ResetPassword";
	}

	@GetMapping("/showRegistrationForm")
	public String showMyLoginPage(Model theModel) {
		User user = new User();
		theModel.addAttribute("user", user);
		return "Authentication/registration-form";
	}
	
	// add request mapping for /access-denied
	
	@GetMapping("/access-denied")
	public String showAccessDenied() {
		
		return "Thymeleaf/access-denied";
		
	}

	@GetMapping("/Dashboard")
	public String Dashboard() {

		return "Thymeleaf/Dashboard";

	}

}









