package com.anthony.mediadatabase.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.anthony.mediadatabase.model.Season;
import com.anthony.mediadatabase.model.SeasonRepository;
import com.anthony.mediadatabase.model.TVShow;
import com.anthony.mediadatabase.model.TVShowRepository;

@Controller
public class TVShowController {

	@Autowired
	TVShowRepository showRepository;

	@Autowired
	SeasonRepository seasonRepository;

	/**
	 * Mapping for the TV show list page
	 */
	@GetMapping("/tvshows")
	public String showPage(Model model) {
		model.addAttribute("shows", showRepository.findAll());
		return "tvShows";
	}

	/**
	 * Mapping for creating a new TV show
	 */
	@GetMapping("/tvshows/new")
	public String newTVShow(Model model) {
		model.addAttribute("tvShow", new TVShow());
		return "tvShowNew";
	}

	/**
	 * Mapping for adding the new TV show to the DB
	 * 
	 * @param tvShow - new TV show to add
	 */
	@PostMapping("/tvshows/new")
	public String newTVShow(@ModelAttribute TVShow tvShow, Model model) {
		showRepository.save(tvShow);
		model.addAttribute("show", tvShow);
		return "tvShowResult";
	}

	@GetMapping("/tvshows/favorites")
	public String getFavoriteMovies(Model model) {
		model.addAttribute("shows", showRepository.findByIsFavorite());
		return "tvShows";
	}

	@GetMapping("/tvshows/watching")
	public String getMoviesBeingWatched(Model model) {
		model.addAttribute("shows", showRepository.findByStatusWatching());
		return "tvShows";
	}

	@GetMapping("/tvshows/watched")
	public String getMoviesWatched(Model model) {
		model.addAttribute("shows", showRepository.findByStatusWatched());
		return "tvShows";
	}

	@GetMapping("/tvshows/towatch")
	public String getMoviesToWatch(Model model) {
		model.addAttribute("shows", showRepository.findByStatusToWatch());
		return "tvShows";
	}

	/**
	 * Mapping for loading the edit page for a TV show
	 * 
	 * @param showId - parameter that holds the id of the TV show to edit
	 */
	@GetMapping("/tvshows/edit")
	public String editTVShow(@RequestParam("tvShowId") Long showId, Model model) {
		Optional<TVShow> tvShow = showRepository.findById(showId);
		if (tvShow.isPresent()) {
			model.addAttribute("show", tvShow.get());
			return "tvShowEdit";
		}
		return "redirect:/tvshows";
	}

	/**
	 * Mapping for editing a TV show
	 * 
	 * @param tvShow - parameter that holds the updated TV show
	 */
	@PostMapping("/tvshows/edit")
	public String updateTVShow(@ModelAttribute TVShow tvShow, Model model) {
		Optional<TVShow> show = showRepository.findById(tvShow.getTvShowId());
		if (show.isPresent()) {
			TVShow updateShow = show.get();
			updateShow.update(tvShow);
			showRepository.save(updateShow);
			model.addAttribute("show", updateShow);
			return "tvShowResult";
		}
		return "redirect:/tvshows";
	}

	/**
	 * Mapping for loading the page to add a season to a TV show
	 * 
	 * @param showId - parameter that holds the selected TV show id
	 */
	@GetMapping("/tvshows/addseason")
	public String addSeason(@RequestParam("tvShowId") Long showId, Model model) {
		Optional<TVShow> tvShow = showRepository.findById(showId);
		if (tvShow.isPresent()) {
			TVShow show = tvShow.get();
			Season newSeason = new Season();
			model.addAttribute("season", newSeason);
			model.addAttribute("tvShow", show.getName());
			model.addAttribute("tvShowId", show.getTvShowId());
			return "seasonNew";
		}
		return "redirect:/tvshows";
	}

	/**
	 * Mapping for adding a season to a TV show
	 * 
	 * @param season   - new season to add
	 * @param tvShowId - id of the TV show to add the season to
	 */
	@PostMapping("/tvshows/addseason")
	public String addSeason(@ModelAttribute Season season, @RequestParam("tvShowId") Long tvShowId, Model model) {
		Optional<TVShow> tvShow = showRepository.findById(tvShowId);
		if (tvShow.isPresent()) {
			TVShow show = tvShow.get();
			show.addSeason(season);
			showRepository.save(show);
			model.addAttribute("show", show);
			return "redirect:/tvshows/edit?tvShowId=" + show.getTvShowId();
		}
		return "redirect:/tvshows";
	}

	/**
	 * Mapping for deleting a season
	 * 
	 * @param seasonId - parameter that holds the id of the season to delete
	 * @param showName - parameter that holds the name of the show the season
	 *                 belongs to
	 * @param showId   - parameter that holds the id of the TV show the season
	 *                 belongs to
	 */
	@GetMapping("/tvshows/deleteseason")
	public String deleteSeason(@RequestParam("seasonId") Long seasonId, @RequestParam("tvShowName") String showName,
			@RequestParam("showId") Long showId, Model model) {
		Optional<Season> season = seasonRepository.findById(seasonId);
		if (season.isPresent()) {
			model.addAttribute("tvShow", showName);
			model.addAttribute("season", season.get());
			model.addAttribute("showId", showId);
			return "seasonDelete";
		}
		return "redirect:/tvshows/edit?tvShowId=" + showId.toString();
	}

	@PostMapping("/tvshows/deleteseason")
	public String deleteSeasonConfirm(@ModelAttribute Season seasonDelete, Model model) {
		Optional<Season> seasonO = seasonRepository.findById(seasonDelete.getSeasonId());
		if (seasonO.isPresent()) {
			Season season = seasonO.get();
			TVShow show = season.getTvShow();
			seasonRepository.delete(season);
			return "redirect:/tvshows/edit?tvShowId=" + show.getTvShowId();
		}
		return "redirect:/tvshows";
	}

	/**
	 * Mapping for loading the delete page for a TV show
	 * 
	 * @param showId - parameter that holds the id of the show to delete
	 */
	@GetMapping("/tvshows/delete")
	public String deleteShow(@RequestParam("tvShowId") Long showId, Model model) {
		Optional<TVShow> show = showRepository.findById(showId);
		if (show.isPresent()) {
			model.addAttribute("show", show.get());
			return "tvShowDelete";
		}
		return "redirect:/tvshows";
	}

	/**
	 * Mapping for deleting a TV show
	 * 
	 * @param showId - parameter that holds the id of the show to delete
	 */
	@GetMapping("/tvshows/delete/confirm")
	public String deleteShowConfirm(@RequestParam("tvShowId") Long showId) {
		Optional<TVShow> show = showRepository.findById(showId);
		if (show.isPresent()) {
			showRepository.deleteById(showId);
		}
		return "redirect:/tvshows";
	}

}
