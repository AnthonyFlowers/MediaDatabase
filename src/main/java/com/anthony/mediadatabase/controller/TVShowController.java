package com.anthony.mediadatabase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.anthony.mediadatabase.model.Season;
import com.anthony.mediadatabase.model.TVShow;
import com.anthony.mediadatabase.model.User;
import com.anthony.mediadatabase.repository.SeasonRepository;
import com.anthony.mediadatabase.repository.TVShowRepository;

@Controller
public class TVShowController extends UserAuthenticatedController {

	@Autowired
	private TVShowRepository showRepository;

	@Autowired
	private SeasonRepository seasonRepository;

	/**
	 * Mapping for the TV show list page
	 * 
	 * @return sends control to the TV show list page with all shows included
	 */
	@GetMapping("/tvshows")
	public String showPage(Model model) {
		model.addAttribute("shows", showRepository.findAllByUserId(getUser().getId()));
		return "tvshow/tvShows";
	}

	/**
	 * Mapping for creating a new TV show
	 * 
	 * @return sends control to the new TV show page
	 */
	@GetMapping("/tvshows/new")
	public String newTVShow(Model model) {
		model.addAttribute("tvShow", new TVShow());
		return "tvshow/new";
	}

	/**
	 * Mapping for adding the new TV show to the DB
	 * 
	 * @param tvShow - new TV show to add
	 * @return sends control to the TV show result page for the newly added TV show
	 */
	@PostMapping("/tvshows/new")
	public String newTVShow(@ModelAttribute TVShow tvShow, Model model) {
		User user = getUser();
		tvShow.setUser(user);
		tvShow.setUserShowId(getNextUserShowId(user));
		showRepository.save(tvShow);
		model.addAttribute("show", tvShow);
		return "tvshow/result";
	}

	/**
	 * Mapping for loading the TV show list page with TV shows marked as "Favorite"
	 * 
	 * @return sends control to the TV show list page with TV shows marked as
	 *         "Favorite"
	 */
	@GetMapping("/tvshows/favorites")
	public String getFavoriteMovies(Model model) {
		model.addAttribute("shows", showRepository.findByIsFavorite(getUser().getId()));
		return "tvshow/tvShows";
	}

	/**
	 * Mapping for loading the TV show list page with TV shows marked as "Watching"
	 * 
	 * @return sends control to the TV show list page with TV shows marked as
	 *         "Watching"
	 */
	@GetMapping("/tvshows/watching")
	public String getMoviesBeingWatched(Model model) {
		model.addAttribute("shows", showRepository.findByStatusWatching(getUser().getId()));
		return "tvshow/tvShows";
	}

	/**
	 * Mapping for loading the TV show list page with TV shows marked as "Watched"
	 * 
	 * @return sends control to the TV show list page with TV shows marked as
	 *         "Watched"
	 */
	@GetMapping("/tvshows/watched")
	public String getMoviesWatched(Model model) {
		model.addAttribute("shows", showRepository.findByStatusWatched(getUser().getId()));
		return "tvshow/tvShows";
	}

	/**
	 * Mapping for loading the TV show list page with TV shows marked as "To-Watch"
	 * 
	 * @return sends control to the TV show list page with TV shows marked as
	 *         "To-Watch"
	 */
	@GetMapping("/tvshows/towatch")
	public String getMoviesToWatch(Model model) {
		model.addAttribute("shows", showRepository.findByStatusToWatch(getUser().getId()));
		return "tvshow/tvShows";
	}

	/**
	 * Mapping for loading the edit page for a TV show
	 * 
	 * @param showId - parameter that holds the id of the TV show to edit
	 * @return sends control to the TV show edit page if the TV show exists or
	 *         redirects to the TV show edit page if the TV show does not exist
	 */
	@GetMapping("/tvshows/edit")
	public String editTVShow(@RequestParam("tvShowId") Long showId, Model model) {
		User user = getUser();
		TVShow tvShow = showRepository.findByUserShowId(user.getId(), showId);
		if (tvShow != null) {
			model.addAttribute("show", tvShow);
			return "tvshow/edit";
		} else {
			model.addAttribute("errorUserShowId", "Could not find a TVShow with that id");
		}
		return "redirect:/tvshows";
	}

	/**
	 * Mapping for editing a TV show
	 * 
	 * @param tvShow - parameter that holds the updated TV show
	 * @return sends control to the show result page if the show exists or redirects
	 *         to the TV show list page if the TV show does not exist
	 */
	@PostMapping("/tvshows/edit")
	public String updateTVShow(@ModelAttribute TVShow tvShow, Model model) {
		User user = getUser();
		TVShow selectedShow = showRepository.findByUserShowId(user.getId(), tvShow.getUserShowId());
		if (selectedShow != null) {
			selectedShow.update(tvShow);
			showRepository.save(selectedShow);
			model.addAttribute("show", selectedShow);
			return "tvshow/result";
		}
		return "redirect:/tvshows";
	}

	/**
	 * Mapping for loading the page to add a season to a TV show
	 * 
	 * @param showId - parameter that holds the selected TV show id
	 * @return sends control to the new season page if the new season's TV show
	 *         exists or redirects to the TV show list page if the new seasons TV
	 *         show does not exist
	 */
	@GetMapping("/tvshows/addseason")
	public String addSeason(@RequestParam("tvShowId") Long showId, Model model) {
		User user = getUser();
		TVShow tvShow = showRepository.findByUserShowId(user.getId(), showId);
		if (tvShow != null) {
			Season newSeason = new Season();
			model.addAttribute("season", newSeason);
			model.addAttribute("tvShow", tvShow.getName());
			model.addAttribute("tvShowId", tvShow.getUserShowId());
			return "tvshow/season/new";
		}
		return "redirect:/tvshows";
	}

	/**
	 * Mapping for adding a season to a TV show
	 * 
	 * @param season   - new season to add
	 * @param tvShowId - id of the TV show to add the season to
	 * 
	 * @return redirects to the edit page of the new season's TV show if the new
	 *         season's show exists or redirects to the TV show list page if the new
	 *         season's TV show does not exist
	 */
	@PostMapping("/tvshows/addseason")
	public String addSeason(@ModelAttribute Season season, @RequestParam("tvShowId") Long tvShowId,
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("error", result);
			return "error";
		}
		User user = getUser();
		TVShow selectedShow = showRepository.findByUserShowId(user.getId(), tvShowId);
		if (selectedShow != null) {
			season.setUserSeasonId(getNextUserSeasonId(user));
			selectedShow.addSeason(season);
			showRepository.save(selectedShow);
			model.addAttribute("show", selectedShow);
			return "redirect:/tvshows/edit?tvShowId=" + selectedShow.getUserShowId();
		}
		return "redirect:/tvshows";
	}

	/**
	 * Mapping for loading the page to delete a season
	 * 
	 * @param seasonId - parameter that holds the id of the season to delete
	 * @param showName - parameter that holds the name of the show the season
	 *                 belongs to
	 * @param showId   - parameter that holds the id of the TV show the season
	 *                 belongs to
	 * 
	 * @return sends control to the delete season page if the season exists or
	 *         redirects to the edit page of the season's TV show if the season does
	 *         not exist
	 */
	@GetMapping("/tvshows/deleteseason")
	public String deleteSeason(@RequestParam("seasonId") Long seasonId, @RequestParam("showId") Long showId,
			Model model) {
		User user = getUser();
		Season season = seasonRepository.findByUserAndSeasonId(user.getId(), seasonId);
		if (season != null) {
			model.addAttribute("tvShow", season.getTvShow().getName());
			model.addAttribute("season", season);
			model.addAttribute("showId", showId);
			return "tvshow/season/delete";
		}
		return "redirect:/tvshows/edit?tvShowId=" + showId.toString();
	}

	/**
	 * Mapping for deleting a season
	 * 
	 * @param seasonDelete - parameter that holds the season to delete
	 * @return redirects to the edit page of the deleted season's TV show if it is
	 *         found or redirects to the TV show list page
	 */
	@PostMapping("/tvshows/deleteseason")
	public String deleteSeasonConfirm(@ModelAttribute Season seasonDelete, Model model) {
		User user = getUser();
		Season season = seasonRepository.findByUserAndSeasonId(user.getId(), seasonDelete.getUserSeasonId());
		if (season != null) {
			TVShow show = season.getTvShow();
			if (show.getCurrentSeason() == season.getSeasonNum()) {
				show.setCurrentSeason(0);
				showRepository.save(show);
			}
			seasonRepository.delete(season);
			return "redirect:/tvshows/edit?tvShowId=" + show.getUserShowId();
		}
		return "redirect:/tvshows";
	}

	/**
	 * Mapping for loading the delete page for a TV show
	 * 
	 * @param showId - parameter that holds the id of the show to delete
	 * @return sends control to the TV show delete page if the TV show exists or
	 *         redirects to the TV show list page if the TV show does not exist
	 */
	@GetMapping("/tvshows/delete")
	public String deleteShow(@RequestParam("tvShowId") Long showId, Model model) {
		User user = getUser();
		TVShow show = showRepository.findByUserShowId(user.getId(), showId);
		if (show != null) {
			model.addAttribute("show", show);
			return "tvshow/delete";
		}
		return "redirect:/tvshows";
	}

	/**
	 * Mapping for deleting a TV show
	 * 
	 * @param showId - parameter that holds the id of the show to delete
	 * @return redirects to the TV show list page after deleting the TV show
	 */
	@GetMapping("/tvshows/delete/confirm")
	public String deleteShowConfirm(@RequestParam("tvShowId") Long showId, Model model) {
		User user = getUser();
		TVShow show = showRepository.findByUserShowId(user.getId(), showId);
		if (show != null) {
			showRepository.delete(show);
		} else {
			model.addAttribute("errorShowNotFound");
		}
		return "redirect:/tvshows";
	}

	// Get the next userSeasonId for a new season
	private Long getNextUserSeasonId(User user) {
		Long latestSeasonId = seasonRepository.findLatestUserSeasonId(user.getId());
		if (latestSeasonId != null)
			return latestSeasonId + 1;
		return 1L;
	}

	/**
	 * Get next userShowId for a new TVShow
	 * 
	 * @param user - User to find the next TVShow id for
	 * @return Long representing the next userShowId for a new TVShow
	 */
	private Long getNextUserShowId(User user) {
		Long latestId = showRepository.findLatestUserShowId(user.getId());
		if (latestId != null)
			return latestId + 1;
		return 1L;
	}

}
