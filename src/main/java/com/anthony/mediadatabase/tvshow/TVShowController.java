package com.anthony.mediadatabase.tvshow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anthony.mediadatabase.season.Season;
import com.anthony.mediadatabase.user.User;
import com.anthony.mediadatabase.user.UserAuthenticatedController;

@Controller
public class TVShowController extends UserAuthenticatedController {

	@Autowired
	TVShowService tvShowService;

	@Autowired
	TVShowValidator tvShowValidator;

	/**
	 * Mapping for creating a new TV show
	 * 
	 * @return sends control to the new TV show page
	 */
	@GetMapping("/tvshows/new")
	public String tvShowNewForm(Model model) {
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
	public String tvShowNewCommit(@ModelAttribute TVShow tvShow, Model model) {
		User user = getUser();
		tvShow.setUser(user);
		tvShowService.save(tvShow);
		model.addAttribute("show", tvShow);
		model.addAttribute("readOnly", true);
		return "tvshow/result";
	}

	/**
	 * Mapping for the TV show list page
	 * 
	 * @return sends control to the TV show list page with all shows included
	 */
	@GetMapping("/tvshows")
	public String tvShowGetAll(Model model) {
		model.addAttribute("shows", tvShowService.findByUserId(getUser().getId()));
		return "tvshow/tvShows";
	}

	/**
	 * Mapping for loading the TV show list page with TV shows marked as "Favorite"
	 * 
	 * @return sends control to the TV show list page with TV shows marked as
	 *         "Favorite"
	 */
	@GetMapping("/tvshows/favorites")
	public String tvShowGetFavorite(Model model) {
		model.addAttribute("shows", tvShowService.findByIsFavorite(getUser().getId()));
		return "tvshow/tvShows";
	}

	/**
	 * Mapping for loading the TV show list page with TV shows marked as "Watching"
	 * 
	 * @return sends control to the TV show list page with TV shows marked as
	 *         "Watching"
	 */
	@GetMapping("/tvshows/watching")
	public String tvShowGetWatching(Model model) {
		model.addAttribute("shows", tvShowService.findByStatus(getUser().getId(), TVStatus.Watching.ordinal()));
		return "tvshow/tvShows";
	}

	/**
	 * Mapping for loading the TV show list page with TV shows marked as "Watched"
	 * 
	 * @return sends control to the TV show list page with TV shows marked as
	 *         "Watched"
	 */
	@GetMapping("/tvshows/watched")
	public String tvShowGetWatched(Model model) {
		model.addAttribute("shows", tvShowService.findByStatus(getUser().getId(), TVStatus.Watched.ordinal()));
		return "tvshow/tvShows";
	}

	/**
	 * Mapping for loading the TV show list page with TV shows marked as "To-Watch"
	 * 
	 * @return sends control to the TV show list page with TV shows marked as
	 *         "To-Watch"
	 */
	@GetMapping("/tvshows/towatch")
	public String tvShowGetToWatch(Model model) {
		model.addAttribute("shows", tvShowService.findByStatus(getUser().getId(), TVStatus.ToWatch.ordinal()));
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
	public String tvShowEdit(@RequestParam("tvShowId") Long showId, Model model, RedirectAttributes ra) {
		User user = getUser();
		TVShow tvShow = tvShowService.findByUserShowId(user.getId(), showId);
		if (tvShow == null) {
			ra.addFlashAttribute("errorNotFound", "Could not find that TV show.");
			return "redirect:/tvshows";
		}
		model.addAttribute("tvShow", tvShow);
		return "tvshow/edit";
	}

	/**
	 * Mapping for editing a TV show
	 * 
	 * @param tvShow - parameter that holds the updated TV show
	 * @return sends control to the show result page if the show exists or redirects
	 *         to the TV show list page if the TV show does not exist
	 */
	@PostMapping("/tvshows/edit")
	public String tvShowEditCommit(@ModelAttribute("tvShow") TVShow tvShow, BindingResult bindingResult, Model model,
			RedirectAttributes ra) {
		tvShowValidator.validate(tvShow, bindingResult);
		if (bindingResult.hasErrors()) {
			return "tvshow/edit";
		}
		User user = getUser();
		TVShow selectedShow = tvShowService.findByUserShowId(user.getId(), tvShow.getUserShowId());
		if (selectedShow == null) {
			ra.addFlashAttribute("errorNotFound", "Could not find that TV show");
			return "redirect:/tvshows";
		}
		selectedShow.update(tvShow);
		tvShowService.save(selectedShow);
		model.addAttribute("show", selectedShow);
		model.addAttribute("readOnly", true);
		return "tvshow/result";
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
	public String tvShowAddSeason(@RequestParam("tvShowId") Long showId, Model model, RedirectAttributes ra) {
		User user = getUser();
		TVShow tvShow = tvShowService.findByUserShowId(user.getId(), showId);
		if (tvShow == null) {
			ra.addFlashAttribute("errorNotFound", "Could not find that TV show.");
			return "redirect:/tvshows";
		}
		Season newSeason = new Season(tvShow);
		model.addAttribute("season", newSeason);
		model.addAttribute("tvShowId", tvShow.getUserShowId());
		return "tvshow/season/new";
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
	public String tvShowAddSeasonCommit(@ModelAttribute Season season, @RequestParam("tvShowId") Long tvShowId,
			BindingResult result, Model model, RedirectAttributes ra) {
		if (result.hasErrors()) {
			ra.addFlashAttribute("error", result);
			return "redirect:/tvshow/season/new";
		}
		User user = getUser();
		tvShowService.addTVShowSeason(user, season, tvShowId);
		return "redirect:/tvshows/edit?tvShowId=" + tvShowId;
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
	public String tvShowDeleteSeason(@RequestParam("seasonId") Long seasonId, @RequestParam("showId") Long showId,
			Model model) {
		User user = getUser();
		Season season = tvShowService.findByUserAndSeasonId(user.getId(), seasonId);
		if (season != null) {
			model.addAttribute("season", season);
			model.addAttribute("readOnly", true);
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
	public String tvShowDeleteSeasonCommit(@ModelAttribute Season seasonDelete, Model model) {
		User user = getUser();
		TVShow tvShow = tvShowService.deleteSeason(user.getId(), seasonDelete.getUserSeasonId());
		return "redirect:/tvshows/edit?tvShowId=" + tvShow.getUserShowId();
	}

	/**
	 * Mapping for loading the delete page for a TV show
	 * 
	 * @param showId - parameter that holds the id of the show to delete
	 * @return sends control to the TV show delete page if the TV show exists or
	 *         redirects to the TV show list page if the TV show does not exist
	 */
	@GetMapping("/tvshows/delete")
	public String tvShowDelete(@RequestParam("tvShowId") Long showId, Model model) {
		User user = getUser();
		TVShow show = tvShowService.findByUserShowId(user.getId(), showId);
		if (show != null) {
			model.addAttribute("show", show);
			model.addAttribute("readOnly", true);
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
	@PostMapping("/tvshows/delete/confirm")
	public String tvShowDeleteCommit(@ModelAttribute("tvShowId") TVShow show, Model model) {
		User user = getUser();
		TVShow showToDelete = tvShowService.findByUserShowId(user.getId(), show.getUserShowId());
		if (showToDelete != null) {
			tvShowService.delete(showToDelete);
		} else {
			model.addAttribute("errorShowNotFound");
		}
		return "redirect:/tvshows";
	}
}
