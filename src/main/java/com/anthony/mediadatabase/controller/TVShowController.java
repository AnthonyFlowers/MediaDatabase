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

import com.anthony.mediadatabase.model.Season;
import com.anthony.mediadatabase.model.TVShow;
import com.anthony.mediadatabase.model.User;
import com.anthony.mediadatabase.repository.MovieRepository;
import com.anthony.mediadatabase.repository.SeasonRepository;
import com.anthony.mediadatabase.repository.TVShowRepository;
import com.anthony.mediadatabase.service.UserService;

@Controller
public class TVShowController {
	@Autowired
	private UserService userService;

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
		return "tvShows";
	}

	/**
	 * Mapping for creating a new TV show
	 * 
	 * @return sends control to the new TV show page
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
	 * @return sends control to the TV show result page for the newly added TV show
	 */
	@PostMapping("/tvshows/new")
	public String newTVShow(@ModelAttribute TVShow tvShow, Model model) {
		User user = getUser();
		tvShow.setUser(user);
		tvShow.setUserShowId(getNextUserShowId(user));
		showRepository.save(tvShow);
		model.addAttribute("show", tvShow);
		return "tvShowResult";
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
		return "tvShows";
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
		return "tvShows";
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
		return "tvShows";
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
		return "tvShows";
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
			return "tvShowEdit";
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
	 * @return sends control to the new season page if the new season's TV show
	 *         exists or redirects to the TV show list page if the new seasons TV
	 *         show does not exist
	 */
	@GetMapping("/tvshows/addseason")
	public String addSeason(@RequestParam("tvShowId") Long showId, Model model) {
		Optional<TVShow> tvShow = showRepository.findById(showId);
		if (tvShow.isPresent()) {
			TVShow show = tvShow.get();
			if (show.getUser().getId() == getUser().getId()) {
				Season newSeason = new Season();
				model.addAttribute("season", newSeason);
				model.addAttribute("tvShow", show.getName());
				model.addAttribute("tvShowId", show.getTvShowId());
				return "seasonNew";
			} else {
				model.addAttribute("errorShowId", "Show with id: " + showId + " could not be found for this user");
			}
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
		Optional<Season> oSeason = seasonRepository.findById(seasonId);
		if (oSeason.isPresent()) {
			Season season = oSeason.get();
			model.addAttribute("tvShow", season.getTvShow().getName());
			model.addAttribute("season", season);
			model.addAttribute("showId", showId);
			return "seasonDelete";
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
	 * @return sends control to the TV show delete page if the TV show exists or
	 *         redirects to the TV show list page if the TV show does not exist
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
	 * @return redirects to the TV show list page after deleting the TV show
	 */
	@GetMapping("/tvshows/delete/confirm")
	public String deleteShowConfirm(@RequestParam("tvShowId") Long showId) {
		Optional<TVShow> show = showRepository.findById(showId);
		if (show.isPresent()) {
			showRepository.deleteById(showId);
		}
		return "redirect:/tvshows";
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

	/**
	 * Get the currently authenticated user
	 * 
	 * @return User that is currently authenticated
	 */
	private User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByUsername(auth.getName());
		return user;
	}

}
