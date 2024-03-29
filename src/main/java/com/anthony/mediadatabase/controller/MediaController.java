package com.anthony.mediadatabase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.anthony.mediadatabase.model.MediaItem;
import com.anthony.mediadatabase.model.User;
import com.anthony.mediadatabase.repository.BookRepository;
import com.anthony.mediadatabase.repository.MediaRepository;
import com.anthony.mediadatabase.repository.MovieRepository;
import com.anthony.mediadatabase.repository.TVShowRepository;

@Controller
public class MediaController extends UserAuthenticatedController {

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private TVShowRepository showRepository;
	
	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private MediaRepository mediaRepository;

	@GetMapping("/media")
	public String mediaMain(Model model) {
		User user = getUser();
		model.addAttribute("movies", movieRepository.findAll(user.getId()));
		model.addAttribute("tvShows", showRepository.findAllByUserId(user.getId()));
		model.addAttribute("books", bookRepository.findAll(user.getId()));
		return "index";
	}

	@GetMapping("/editMedia")
	public String editMedia(@RequestParam(name = "selectedMedia", required = true) Integer mediaId, Model model) {
		MediaItem selectedItem = mediaRepository.findById(mediaId);
		model.addAttribute("mediaItem", selectedItem);
		return "editMedia";
	}
}
