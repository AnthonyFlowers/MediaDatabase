package com.anthony.mediadatabase.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.anthony.mediadatabase.model.Movie;
import com.anthony.mediadatabase.model.User;
import com.anthony.mediadatabase.repository.MovieRepository;
import com.anthony.mediadatabase.service.UserService;

@Controller
public class MovieController extends UserAuthenticatedController {
	@Autowired
	private UserService userService;

	@Autowired
	private MovieRepository movieRepository;

	/**
	 * Mapping for the movie list page
	 * 
	 * @return sends control to the movie list page with all movies included
	 */
	@GetMapping("/movies")
	public String moviePage(Model model) {
		model.addAttribute("movies", movieRepository.findAll(getUser().getId()));
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
		Movie newMovie = new Movie();
		User user = getUser();
		Long nextUserMovieId = getNextUserMovieId(user);
		newMovie.updateMovie(movie);
		newMovie.setUser(user);
		newMovie.setUserMovieId(nextUserMovieId);
		movieRepository.save(newMovie);
		model.addAttribute("movie", newMovie);
		return "movieResult";
	}

	/**
	 * Mapping for the movie favorites list page
	 * 
	 * @return sends control to the movie list page with movies marked as "Favorite"
	 */
	@GetMapping("/movies/favorites")
	public String getFavoriteMovies(Model model) {
		model.addAttribute("movies", movieRepository.findByIsFavorite(getUser().getId()));
		return "movies";
	}

	/**
	 * Mapping for the movie watching list page
	 * 
	 * @return sends control to the movie list page with movies marked as "Watching"
	 */
	@GetMapping("/movies/watching")
	public String getMoviesBeingWatched(Model model) {
		model.addAttribute("movies", movieRepository.findByStatusWatching(getUser().getId()));
		return "movies";
	}

	/**
	 * Mapping for the movie watched list page
	 * 
	 * @return sends control to the movie list page with movies marked as "Watched"
	 */
	@GetMapping("/movies/watched")
	public String getMoviesWatched(Model model) {
		model.addAttribute("movies", movieRepository.findByStatusWatched(getUser().getId()));
		return "movies";
	}

	/**
	 * Mapping for the movie to watch list page
	 * 
	 * @return sends control to the movie list page with movies marked as "To-Watch"
	 */
	@GetMapping("/movies/towatch")
	public String getMoviesToWatch(Model model) {
		model.addAttribute("movies", movieRepository.findByStatusToWatch(getUser().getId()));
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
	public String editMovie(@RequestParam("selectedMovie") Long movieId, Model model) {
		User user = getUser();
		Movie selectedMovie = movieRepository.findByUserMovieId(user.getId(), movieId);
		if (selectedMovie != null) {
			model.addAttribute("movie", selectedMovie);
			return "movieEdit";
		} else {
			model.addAttribute("errorMovieId", "Could not find the movie with that id for the current user.");
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
		User user = getUser();
		Movie selectedMovie = movieRepository.findByUserMovieId(user.getId(), movie.getUserMovieId());
		if (selectedMovie != null) {
			selectedMovie.updateMovie(movie);
			movieRepository.save(selectedMovie);
			return "movieResult";
		} else {
			model.addAttribute("errorMovieId", "Could not find the movie with that id for the current user.");
		}
		return "redirect:/movies";
	}

	/**
	 * Mapping for loading the delete movie page
	 * 
	 * @param movieId - holds the id of the movie to delete
	 * @return sends control to the delete movie page or redirects to the movie list
	 *         page if the movie does not exist
	 */
	@GetMapping("/movies/delete")
	public String deleteMovieForm(@RequestParam("selectedMovie") Long movieId, Model model) {
		User user = getUser();
		Movie selectedMovie = movieRepository.findByUserMovieId(user.getId(), movieId);
		if (selectedMovie != null) {
			model.addAttribute("movie", selectedMovie);
			return "movieDelete";
		} else {
			model.addAttribute("errorMovieId", "Could not find the movie with that id for the current user.");
		}
		return "redirect:/movies";
	}

	/**
	 * Mapping for deleting a movie
	 * 
	 * @param movieId - holds the id of the movie to delete
	 * @return redirects to the movie list page
	 */
	@PostMapping("/movies/delete/confirm")
	public String deleteMovie(@ModelAttribute("movie") Movie movie, Model model) {
		User user = getUser();
		Movie movieToDelete = movieRepository.findByUserMovieId(user.getId(), movie.getUserMovieId());
		movieRepository.delete(movieToDelete);
		model.addAttribute("infoDeleteSuccess", "Movie deletion successful.");
		return "redirect:/movies";
	}

	// Get the next userMovieId for a new movie
	private long getNextUserMovieId(User user) {
		Long latestId = movieRepository.findLatestUserMovieId(user.getId());
		if (latestId != null)
			return latestId + 1;
		return 1L;
	}
}
