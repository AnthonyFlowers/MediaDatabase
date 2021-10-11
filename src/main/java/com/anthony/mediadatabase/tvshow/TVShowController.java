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

import com.anthony.mediadatabase.media.MediaItemValidator;
import com.anthony.mediadatabase.season.Season;
import com.anthony.mediadatabase.user.User;
import com.anthony.mediadatabase.user.UserAuthenticatedController;

@Controller
public class TVShowController extends UserAuthenticatedController {

	@Autowired
	TVShowService tvShowService;

	@Autowired
	MediaItemValidator mediaValidator;

	@Autowired
	TVShowValidator tvShowValidator;

	/**
	 * Mapping for creating a new TV show
	 */
	@GetMapping("/tvshows/new")
	public String tvShowNewForm(Model model) {
		model.addAttribute("tvShow", new TVShow());
		return "tvshow/new";
	}

	/**
	 * Mapping for adding the new TV show to the DB
	 */
	@PostMapping("/tvshows/new")
	public String tvShowNewCommit(@ModelAttribute("tvShow") TVShow tvShow, BindingResult result, Model model) {
		validateTvShow(tvShow, result);
		if (result.hasErrors()) {
			model.addAttribute("tvShow", tvShow);
			return "tvshow/new";
		}
		tvShow.setUser(getUser());
		tvShowService.save(tvShow);
		model.addAttribute("show", tvShow);
		model.addAttribute("readOnly", true);
		return "tvshow/result";
	}

	/**
	 * Mapping for the TV show list page
	 */
	@GetMapping("/tvshows")
	public String tvShowGetAll(Model model) {
		model.addAttribute("shows", tvShowService.findByUserId(getUser().getId()));
		return "tvshow/tvShows";
	}

	/**
	 * Mapping for loading the TV show list page with TV shows marked as "Favorite"
	 */
	@GetMapping("/tvshows/favorites")
	public String tvShowGetFavorite(Model model) {
		model.addAttribute("shows", tvShowService.findByIsFavorite(getUser().getId()));
		return "tvshow/tvShows";
	}

	/**
	 * Mapping for loading the TV show list page with TV shows marked as "Watching"
	 */
	@GetMapping("/tvshows/watching")
	public String tvShowGetWatching(Model model) {
		model.addAttribute("shows", tvShowService.findByStatus(getUser().getId(), TVStatus.Watching.ordinal()));
		return "tvshow/tvShows";
	}

	/**
	 * Mapping for loading the TV show list page with TV shows marked as "Watched"
	 */
	@GetMapping("/tvshows/watched")
	public String tvShowGetWatched(Model model) {
		model.addAttribute("shows", tvShowService.findByStatus(getUser().getId(), TVStatus.Watched.ordinal()));
		return "tvshow/tvShows";
	}

	/**
	 * Mapping for loading the TV show list page with TV shows marked as "To-Watch"
	 */
	@GetMapping("/tvshows/towatch")
	public String tvShowGetToWatch(Model model) {
		model.addAttribute("shows", tvShowService.findByStatus(getUser().getId(), TVStatus.ToWatch.ordinal()));
		return "tvshow/tvShows";
	}

	/**
	 * Mapping for loading the edit page for a TV show
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
	 */
	@PostMapping("/tvshows/edit")
	public String tvShowEditCommit(@ModelAttribute("tvShow") TVShow tvShow, BindingResult result, Model model,
			RedirectAttributes ra) {
		validateTvShow(tvShow, result);
		if (result.hasErrors()) {
			return "tvshow/edit";
		}
		TVShow selectedShow = tvShowService.findByUserShowId(getUser().getId(), tvShow.getUserShowId());
		if (selectedShow == null) {
			ra.addFlashAttribute("errorNotFound", "Could not find that TV show.");
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
	 */
	@GetMapping("/tvshows/addseason")
	public String tvShowAddSeason(@RequestParam("tvShowId") Long showId, Model model, RedirectAttributes ra) {
		User user = getUser();
		TVShow tvShow = tvShowService.findByUserShowId(user.getId(), showId);
		if (tvShow == null) {
			ra.addAttribute("errorNotFound", "Could not find the TV show for that season.");
			return "redirect:/tvshow/tvShows";
		}
		Season newSeason = new Season(tvShow);
		model.addAttribute("season", newSeason);
		model.addAttribute("tvShowId", tvShow.getUserShowId());
		return "tvshow/season/new";
	}

	/**
	 * Mapping for adding a season to a TV show
	 */
	@PostMapping("/tvshows/addseason")
	public String tvShowAddSeasonCommit(@ModelAttribute Season season, @RequestParam("tvShowId") Long tvShowId,
			Model model, RedirectAttributes ra) {
		TVShow tvShow = tvShowService.findByUserShowId(getUser().getId(), tvShowId);
		if (tvShow == null) {
			ra.addFlashAttribute("errorNotFound", "The TV show for that season was not found.");
			return "redirect:/tvshows";
		}
		season.setTvShow(tvShow);
		User user = getUser();
		if (!tvShowService.addTVShowSeason(user, season, tvShowId)) {
			ra.addFlashAttribute("errorDuplicateSeason", "That season already exists.");
		}
		return "redirect:/tvshows/edit?tvShowId=" + tvShowId;
	}

	/**
	 * Mapping for loading the page to delete a season
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
	 */
	@PostMapping("/tvshows/deleteseason")
	public String tvShowDeleteSeasonCommit(@ModelAttribute("season") Season seasonDelete, Model model) {
		User user = getUser();
		Long userTvShowId = tvShowService.deleteSeason(user.getId(), seasonDelete.getUserSeasonId());
		return "redirect:/tvshows/edit?tvShowId=" + userTvShowId;
	}

	/**
	 * Mapping for loading the delete page for a TV show
	 */
	@GetMapping("/tvshows/delete")
	public String tvShowDelete(@RequestParam("tvShowId") Long showId, Model model, RedirectAttributes ra) {
		User user = getUser();
		TVShow show = tvShowService.findByUserShowId(user.getId(), showId);
		if (show == null) {
			ra.addFlashAttribute("errorNotFound", "Could not find that TV show.");
			return "redirect:/tvshows";
		}
		model.addAttribute("show", show);
		model.addAttribute("readOnly", true);
		return "tvshow/delete";
	}

	/**
	 * Mapping for deleting a TV show
	 */
	@PostMapping("/tvshows/delete/confirm")
	public String tvShowDeleteCommit(@ModelAttribute("tvShowId") TVShow show, Model model, RedirectAttributes ra) {
		User user = getUser();
		TVShow showToDelete = tvShowService.findByUserShowId(user.getId(), show.getUserShowId());
		if (showToDelete == null) {
			ra.addFlashAttribute("errorNotFound", "Could not find that TV show.");
			return "redirect:/tvshows";
		}
		tvShowService.delete(showToDelete);
		return "redirect:/tvshows";
	}

	private void validateTvShow(TVShow tvShow, BindingResult result) {
		mediaValidator.validate(tvShow.getMediaItem(), result);
		tvShowValidator.validate(tvShow, result);
	}
}
