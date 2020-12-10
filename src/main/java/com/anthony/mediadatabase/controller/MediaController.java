package com.anthony.mediadatabase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.anthony.mediadatabase.model.MediaItem;
import com.anthony.mediadatabase.model.Movie;
import com.anthony.mediadatabase.model.User;
import com.anthony.mediadatabase.repository.MediaRepository;
import com.anthony.mediadatabase.repository.MovieRepository;
import com.anthony.mediadatabase.service.UserService;

@Controller
public class MediaController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private MediaRepository mediaRepository;
	
	@GetMapping("/media")
	public String mediaMain(Model model) {
		model.addAttribute("movies", movieRepository.findAll(getUser().getId()));
		return "index";
	}
	
	@GetMapping("/editMedia")
	public String editMedia(@RequestParam(name = "selectedMedia", required = true) Integer mediaId, Model model) {
		MediaItem selectedItem = mediaRepository.findById(mediaId);
		model.addAttribute("mediaItem", selectedItem);
		return "editMedia";
	}
	
	// Gets the currently authenticated user
	private User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByUsername(auth.getName());
		return user;
	}
}
