package com.anthony.mediadatabase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.anthony.mediadatabase.security.SecurityService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private UserValidator userValidator;

	@GetMapping("/registration")
	public String userRegister(Model model) {
		model.addAttribute("userForm", new User());
		return "registration";
	}

	@PostMapping("/registration")
	public String userRegisterCommit(@ModelAttribute("userForm") User userForm, BindingResult result) {
		userValidator.validate(userForm, result);
		if (result.hasErrors()) {
			return "registration";
		}

		userService.save(userForm);
		securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());
		return "redirect:/welcome";
	}

	@GetMapping("/login")
	public String userLogin(Model model, String error, String logout) {
		if (error != null) {
			model.addAttribute("error");
		}
		if (logout != null) {
			SecurityContextHolder.clearContext();
			model.addAttribute("logout");
		}
		return "login";
	}

	@GetMapping("/welcome")
	public String userWelcome(Model model) {
		return "welcome";
	}
}