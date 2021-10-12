package com.anthony.mediadatabase.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.anthony.mediadatabase.book.BookRepository;
import com.anthony.mediadatabase.movie.MovieRepository;
import com.anthony.mediadatabase.tvshow.TVShowRepository;
import com.anthony.mediadatabase.user.User;
import com.anthony.mediadatabase.user.UserAuthenticatedController;

@Controller
public class MediaController extends UserAuthenticatedController {

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private TVShowRepository showRepository;

	@Autowired
	private BookRepository bookRepository;

	@GetMapping({ "/", "/media" })
	public String mediaMain(Model model) {
		User user = getUser();
		model.addAttribute("movies", movieRepository.findAll(user.getId()));
		model.addAttribute("tvShows", showRepository.findAllByUserId(user.getId()));
		model.addAttribute("books", bookRepository.findAll(user.getId()));
		return "index";
	}
}
