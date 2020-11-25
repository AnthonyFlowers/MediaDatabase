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
import com.anthony.mediadatabase.model.TVShow;
import com.anthony.mediadatabase.model.TVShowRepository;

@Controller
public class TVShowController {

	@Autowired
	TVShowRepository showRepository;

	@GetMapping("/tvshows")
	public String showPage(Model model) {
		model.addAttribute("shows", showRepository.findAll());
		return "tvShows";
	}

	@GetMapping("/tvshows/new")
	public String newTVShow(Model model) {
		model.addAttribute("tvShow", new TVShow());
		return "newTVShow";
	}

	@PostMapping("/tvshows/new")
	public String newTVShow(@ModelAttribute TVShow tvShow, Model model) {
		showRepository.save(tvShow);
		model.addAttribute("show", tvShow);
		return "tvShowResult";
	}

	@GetMapping("/tvshows/edit")
	public String editTVShow(@RequestParam("tvShowId") Long showId, Model model) {
		Optional<TVShow> tvShow = showRepository.findById(showId);
		if (tvShow.isPresent()) {
			model.addAttribute("show", tvShow.get());
			return "tvShowEdit";
		}
		return "redirect:/tvshows";
	}

	@PostMapping("/tvshows/update")
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
	 * Load the page for adding a new Season
	 * @param showId - the id if the show to add a season to
	 * @param model - holds the attributes for the page
	 * @return String - name of the template to load
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
			return "newSeason";
		}
		return "redirect:/tvshows";
	}

	@PostMapping("/tvshows/addseason")
	public String addSeason(@ModelAttribute Season season, @RequestParam("tvShowId") Long tvShowId, Model model) {
		Optional<TVShow> tvShow = showRepository.findById(tvShowId);
		if(tvShow.isPresent()) {
			TVShow show = tvShow.get();
			show.addSeason(season);
			showRepository.save(show);
			model.addAttribute("show", show);
			return "redirect:/tvshows/edit?tvShowId=" + show.getTvShowId();
		}
		return "redirect:/tvshows";
	}
	
	@GetMapping("/tvshows/delete")
	public String deleteShow(@RequestParam("tvShowId") Long showId, Model model) {
		Optional<TVShow> show = showRepository.findById(showId);
		if(show.isPresent()) {
			model.addAttribute("show", show.get());
			return "tvShowDelete";
		}
		return "redirect:/tvshows";
	}
	
	@GetMapping("/tvshows/delete/confirm")
	public String deleteShowConfirm(@RequestParam("tvShowId") Long showId) {
		Optional<TVShow> show = showRepository.findById(showId);
		if(show.isPresent()) {
			showRepository.deleteById(showId);
		}
		return "redirect:/tvshows";
	}

}
