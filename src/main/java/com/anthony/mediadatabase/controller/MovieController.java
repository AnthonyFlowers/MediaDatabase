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

	/**
	 * Mapping for the movie list page
	 * 
	 * @return sends control to the movie list page with all movies included
	 */
	@GetMapping("/movies")
	public String moviePage(Model model) {
		model.addAttribute("movies", movieRepository.findAll());
		return "movies";
	}

	/**
	 * Mapping for loading the page to add a new movie
	 * 
	 * @return sends control to the new movie page
	 */
	@GetMapping("/movies/new")
	public String newMovieForm(Model model) {
		model.addAttribute("movie", new Movie());
		return "movieNew";
	}

	/**
	 * Mapping for adding a new movie to the DB
	 * 
	 * @param movie - parameter that holds the new movie to add
	 * @return sends control to the movie result page of the new movie
	 */
	@PostMapping("/movies/new")
	public String newMovie(@ModelAttribute Movie movie, Model model) {
		movieRepository.save(movie);
		model.addAttribute("movie", movie);
		return "movieResult";
	}

	/**
	 * Mapping for the movie favorites list page
	 * 
	 * @return sends control to the movie list page with movies marked as "Favorite"
	 */
	@GetMapping("/movies/favorites")
	public String getFavoriteMovies(Model model) {
		model.addAttribute("movies", movieRepository.findByIsFavorite());
		return "movies";
	}

	/**
	 * Mapping for the movie watching list page
	 * 
	 * @return sends control to the movie list page with movies marked as "Watching"
	 */
	@GetMapping("/movies/watching")
	public String getMoviesBeingWatched(Model model) {
		model.addAttribute("movies", movieRepository.findByStatusWatching());
		return "movies";
	}

	/**
	 * Mapping for the movie watched list page
	 * 
	 * @return sends control to the movie list page with movies marked as "Watched"
	 */
	@GetMapping("/movies/watched")
	public String getMoviesWatched(Model model) {
		model.addAttribute("movies", movieRepository.findByStatusWatched());
		return "movies";
	}

	/**
	 * Mapping for the movie to watch list page
	 * 
	 * @return sends control to the movie list page with movies marked as "To-Watch"
	 */
	@GetMapping("/movies/towatch")
	public String getMoviesToWatch(Model model) {
		model.addAttribute("movies", movieRepository.findByStatusToWatch());
		return "movies";
	}

	/**
	 * Mapping for loading the edit movie page
	 * 
	 * @param movieId - parameter that holds the id of the movie to edit
	 * @return sends control to the movie edit page of the selected movie or
	 *         redirects to the movie list page if the movie does not exist
	 */
	@GetMapping("/movies/edit")
	public String editMovie(@RequestParam("selectedMovie") String movieId, Model model) {
		Optional<Movie> selectedMovie = movieRepository.findById(Long.valueOf(movieId));
		if (selectedMovie.isPresent()) {
			model.addAttribute("movie", selectedMovie.get());
			return "movieEdit";
		}
		return "redirect:/movies";
	}

	/**
	 * Mapping for editing a movie
	 * 
	 * @param movie - holds the edited movie
	 * @return sends control to the movie result page of the edited movie
	 */
	@PostMapping("/movies/edit")
	public String updateMove(@ModelAttribute Movie movie, Model model) {
		movieRepository.save(movie);
		return "movieResult";
	}

	/**
	 * Mapping for loading the delete movie page
	 * 
	 * @param movieId - holds the id of the movie to delete
	 * @return sends control to the delete movie page or redirects to the movie list
	 *         page if the movie does not exist
	 */
	@GetMapping("/movies/delete")
	public String deleteMovieForm(@RequestParam("selectedMovie") String movieId, Model model) {
		Optional<Movie> selectedMovie = movieRepository.findById(Long.valueOf(movieId));
		if (selectedMovie.isPresent()) {
			model.addAttribute("movie", selectedMovie.get());
			return "movieDelete";
		}
		return "redirect:/movies";
	}

	/**
	 * Mapping for deleting a movie
	 * 
	 * @param movieId - holds the id of the movie to delete
	 * @return redirects to the movie list page
	 */
	@GetMapping("/movies/delete/confirm")
	public String deleteMovie(@RequestParam("movieId") String movieId) {
		try {
			Long parsedMovieId = Long.parseLong(movieId);
			movieRepository.deleteById(parsedMovieId);
		} catch (NumberFormatException e) {
			System.out.println("Movie id could not be parsed");
		}
		return "redirect:/movies";
	}
}