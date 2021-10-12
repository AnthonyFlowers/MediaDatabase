package com.anthony.mediadatabase.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anthony.mediadatabase.media.MediaItemValidator;
import com.anthony.mediadatabase.movie.Movie.MovieStatus;
import com.anthony.mediadatabase.user.User;
import com.anthony.mediadatabase.user.UserAuthenticatedController;

@Controller
public class MovieController extends UserAuthenticatedController {

	@Autowired
	MovieService movieService;

	@Autowired
	MediaItemValidator mediaValidator;
	@Autowired
	MovieValidator movieValidator;

	/**
	 * Mapping for loading the page to add a new movie
	 */
	@GetMapping("/movies/new")
	public String movieNewForm(Model model) {
		model.addAttribute("movie", new Movie());
		return "movie/new";
	}

	/**
	 * Mapping for adding a new movie
	 */
	@PostMapping("/movies/new")
	public String movieNewCommit(@ModelAttribute Movie movie, BindingResult result, Model model) {
		User user = getUser();
		movie.setUser(user);
		validateMovie(movie, result);
		if (result.hasErrors()) {
			return "movie/new";
		}
		movieService.save(movie);
		model.addAttribute("movie", movie);
		model.addAttribute("readOnly", true);
		return "movie/result";
	}

	/**
	 * Mapping for the movie list page
	 */
	@GetMapping("/movies")
	public String movieGetAll(Model model) {
		model.addAttribute("movies", movieService.findAll(getUser().getId()));
		return "movie/movies";
	}

	/**
	 * Mapping for the movie favorites list page
	 */
	@GetMapping("/movies/favorites")
	public String movieGetFavorite(Model model) {
		model.addAttribute("movies", movieService.findByIsFavorite(getUser().getId()));
		return "movie/movies";
	}

	/**
	 * Mapping for the movie watching list page
	 */
	@GetMapping("/movies/watching")
	public String movieGetWatching(Model model) {
		model.addAttribute("movies", movieService.findByStatus(getUser().getId(), MovieStatus.Watching));
		return "movie/movies";
	}

	/**
	 * Mapping for the movie watched list page
	 */
	@GetMapping("/movies/watched")
	public String movieGetWatched(Model model) {
		model.addAttribute("movies", movieService.findByStatus(getUser().getId(), MovieStatus.Watched));
		return "movie/movies";
	}

	/**
	 * Mapping for the movie to watch list page
	 */
	@GetMapping("/movies/towatch")
	public String movieGetToWatch(Model model) {
		model.addAttribute("movies", movieService.findByStatus(getUser().getId(), MovieStatus.ToWatch));
		return "movie/movies";
	}

	/**
	 * Mapping for loading the edit movie page
	 */
	@GetMapping("/movies/edit")
	public String movieEdit(@RequestParam("movieId") Long movieId, Model model, RedirectAttributes ra) {
		User user = getUser();
		Movie selectedMovie = movieService.findByUserMovieId(user.getId(), movieId);
		if (selectedMovie == null) {
			ra.addFlashAttribute("errorNotFound", "Could not find that Movie.");
			return "redirect:/movies";
		}
		model.addAttribute("movie", selectedMovie);
		return "movie/edit";
	}

	/**
	 * Mapping for editing a movie
	 */
	@PostMapping("/movies/edit")
	public String movieEditCommit(@ModelAttribute("movie") Movie movie, BindingResult result, Model model,
			RedirectAttributes ra) {
		User user = getUser();
		Movie selectedMovie = movieService.findByUserMovieId(user.getId(), movie.getUserMovieId());
		if (selectedMovie == null) {
			ra.addFlashAttribute("errorNotFound", "Could not find that Movie.");
			return "redirect:/movies";
		}
		validateMovie(movie, result);
		if (result.hasErrors()) {
			model.addAttribute("movie", movie);
			return "movie/edit";
		}
		selectedMovie.updateMovie(movie);
		movieService.save(selectedMovie);
		model.addAttribute("readOnly", true);
		return "movie/result";
	}

	/**
	 * Mapping for loading the delete movie page
	 */
	@GetMapping("/movies/delete")
	public String movieDeleteForm(@RequestParam("movieId") Long movieId, Model model, RedirectAttributes ra) {
		User user = getUser();
		Movie selectedMovie = movieService.findByUserMovieId(user.getId(), movieId);
		if (selectedMovie == null) {
			ra.addFlashAttribute("errorNotFound", "Could not find that Movie.");
			return "redirect:/movies";
		}
		model.addAttribute("movie", selectedMovie);
		model.addAttribute("readOnly", true);
		return "movie/delete";

	}

	/**
	 * Mapping for deleting a movie
	 */
	@PostMapping("/movies/delete/confirm")
	public String movieDeleteCommit(@ModelAttribute("movie") Movie movie, Model model, RedirectAttributes ra) {
		User user = getUser();
		Movie movieToDelete = movieService.findByUserMovieId(user.getId(), movie.getUserMovieId());
		if (movieToDelete == null) {
			ra.addFlashAttribute("errorNotFound", "Could not find that movie.");
			return "redirect:/movies";
		}
		movieService.delete(movieToDelete);
		ra.addFlashAttribute("infoSuccess", "Movie deletion successful.");
		return "redirect:/movies";
	}

	private void validateMovie(Movie movie, BindingResult result) {
		mediaValidator.validate(movie.getMediaItem(), result);
		movieValidator.validate(movie, result);
	}
}
