package com.anthony.mediadatabase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.anthony.mediadatabase.model.AjaxResponseBody;
import com.anthony.mediadatabase.model.Season;
import com.anthony.mediadatabase.model.User;
import com.anthony.mediadatabase.repository.SeasonRepository;

@RestController
public class UpdateMediaController extends UserAuthenticatedController {

	@Autowired
	private SeasonRepository seasonRepository;

	@PostMapping("/tvshow/season/episode/increment")
	public ResponseEntity<?> incrementSeasonEpisode(@RequestBody Long userSeasonId) {
		User user = getUser();
		AjaxResponseBody result = new AjaxResponseBody();

		Season season = seasonRepository.findByUserAndSeasonId(user.getId(), userSeasonId);
		if (season == null) {
			result.setMsg("Season not found!");
			return ResponseEntity.badRequest().body(result);
		}
		season.incrementEpisode();
		result.setMsg("success");
		return ResponseEntity.ok(result);
	}

	@PostMapping("/tvshow/season/set")
	public ResponseEntity<?> setCurrentSeason(@RequestBody Long userSeasonId) {
		User user = getUser();
		AjaxResponseBody result = new AjaxResponseBody();

		Season season = seasonRepository.findByUserAndSeasonId(user.getId(), userSeasonId);
		if (season == null) {
			result.setMsg("Season not found!");
			return ResponseEntity.badRequest().body(result);
		}
		season.getTvShow().setCurrentSeason(season.getSeasonNum());
		result.setMsg("success");
		return ResponseEntity.ok(result);
	}
}