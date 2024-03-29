package com.anthony.mediadatabase.season;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.anthony.mediadatabase.media.AjaxResponseBody;
import com.anthony.mediadatabase.user.User;
import com.anthony.mediadatabase.user.UserAuthenticatedController;

@RestController
public class SeasonRestController extends UserAuthenticatedController {

	@Autowired
	private SeasonRepository seasonRepository;

	@PostMapping("/tvshows/season/episode/increment")
	public ResponseEntity<?> seasonEpisodeIncrement(@RequestBody Map<String, Long> ajaxRequestBody) {
		User user = getUser();
		AjaxResponseBody result = new AjaxResponseBody();

		Season season = seasonRepository.findByUserAndSeasonId(user.getId(), ajaxRequestBody.get("userSeasonId"));
		if (season == null) {
			result.setMsg("Season not found!");
			return ResponseEntity.badRequest().body(result);
		}
		season.incrementEpisode();
		seasonRepository.save(season);
		result.setMsg("success");
		result.setEpisodeNum(season.getEpisode());
		return ResponseEntity.ok(result);
	}

	@PostMapping("/tvshows/season/episode/decrement")
	public ResponseEntity<?> seasonEpisodeDecrement(@RequestBody Map<String, Long> ajaxRequestBody) {
		User user = getUser();
		AjaxResponseBody result = new AjaxResponseBody();

		Season season = seasonRepository.findByUserAndSeasonId(user.getId(), ajaxRequestBody.get("userSeasonId"));
		if (season == null) {
			result.setMsg("Season not found!");
			return ResponseEntity.badRequest().body(result);
		}
		season.decrementEpisode();
		seasonRepository.save(season);
		result.setMsg("success");
		result.setEpisodeNum(season.getEpisode());
		return ResponseEntity.ok(result);
	}
}