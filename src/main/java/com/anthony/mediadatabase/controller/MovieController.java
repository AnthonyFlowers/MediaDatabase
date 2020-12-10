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
public class MovieController {
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

	private long getNextUserMovieId(User user) {
		Optional<Long> nextId = Optional.ofNullable(movieRepository.findNextUserMovieId(user.getId()));
		return nextId.orElse(0L) + 1;
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
			if (selectedMovie.getUser().getId() == user.getId()) {
				model.addAttribute("movie", selectedMovie);
				return "movieEdit";
			}
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
		if(selectedMovie != null) {
			if(selectedMovie.getUser().getId() == user.getId()) {
				selectedMovie.updateMovie(movie);
				movieRepository.save(selectedMovie);
				return "movieResult";
			}
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
	public String deleteMovie(@RequestParam("movieId") String movieId, Model model) {
		try {
			Long parsedMovieId = Long.parseLong(movieId);
			movieRepository.deleteById(parsedMovieId);
		} catch (NumberFormatException e) {
			System.out.println("Movie id could not be parsed");
			model.addAttribute("errorFindingMovieId", "An error occured while trying to find the movie with id: " + movieId);
		}
		return "redirect:/movies";
	}
	
	// Gets the currently authenticated user
	private User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByUsername(auth.getName());
		return user;
	}
}
