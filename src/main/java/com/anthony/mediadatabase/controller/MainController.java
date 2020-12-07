package com.anthony.mediadatabase.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
	@SuppressWarnings("unused")
	private static final Logger Log = LoggerFactory.getLogger(MainController.class);
	
	@GetMapping("/")
	public String main() {
		return "redirect:/media";
	}
	
//	@PostMapping("/login")
//	public String login() {
//		return "login";
//	}

}
