package com.anthony.mediadatabase.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.anthony.mediadatabase.model.MediaItem;
import com.anthony.mediadatabase.model.MediaRepository;
import com.anthony.mediadatabase.model.Movie;
import com.anthony.mediadatabase.model.MovieRepository;

@Controller
public class MovieController {

	@Autowired
	private MovieRepository movieRepository;

	@GetMapping("/movies")
	public String moviePage(Model model) {
		model.addAttribute("movies", movieRepository.findAll());
		return "movies";
	}

	@GetMapping("/movies/new")
	public String newMovieForm(Model model) {
		model.addAttribute("movie", new Movie());
		return "newMovie";
	}

	@PostMapping("/movies/new")
	public String newMovie(@ModelAttribute Movie movie, Model model) {
		movieRepository.save(movie);
		model.addAttribute("movie", movie);
		return "result";
	}

	@GetMapping("/movies/favorites")
	public String getFavoriteMovies(Model model) {
		model.addAttribute("movies", movieRepository.findByIsFavorite());
		return "movies";
	}
	
	@GetMapping("/movies/watching")
	public String getMoviesBeingWatched(Model model) {
		model.addAttribute("movies", movieRepository.findByStatusWatching());
		return "movies";
	}
	
	@GetMapping("/movies/watched")
	public String getMoviesWatched(Model model) {
		model.addAttribute("movies", movieRepository.findByStatusWatched());
		return "movies";
	}
	
	@GetMapping("/movies/towatch")
	public String getMoviesToWatch(Model model) {
		model.addAttribute("movies", movieRepository.findByStatusToWatch());
		return "movies";
	}

	@GetMapping("/movies/edit")
	public String editMovie(@RequestParam("selectedMovie") String movieId, Model model) {
		Optional<Movie> selectedMovie = movieRepository.findById(Long.valueOf(movieId));
		if (selectedMovie.isPresent()) {
			model.addAttribute("movie", selectedMovie.get());
			return "editMovie";
		}
		return "redirect:/movies";
	}

	@PostMapping("/movies/update")
	public String updateMove(@ModelAttribute Movie movie, Model model) {
		movieRepository.save(movie);
		return "result";
	}

	@GetMapping("/movies/delete")
	public String deleteMovieForm(@RequestParam("selectedMovie") String movieId, Model model) {
		Optional<Movie> selectedMovie = movieRepository.findById(Long.valueOf(movieId));
		if(selectedMovie.isPresent()) {
			model.addAttribute("movie", selectedMovie.get());
			return "deleteMovieConfirmation";
		}
		return "redirect:/movies";
	}

	@GetMapping("/movies/delete/confirm")
	public String deleteMovie(@RequestParam("movieId") String movieId) {
		try {
			Long parsedMovieId = Long.parseLong(movieId);
			movieRepository.deleteById(parsedMovieId);
		} catch (NumberFormatException e){
			System.out.println("Movie id could not be parsed");
		}
		return "redirect:/movies";
	}
}
