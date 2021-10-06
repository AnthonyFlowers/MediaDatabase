package com.anthony.mediadatabase.tvshow;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.anthony.mediadatabase.media.AjaxResponseBody;
import com.anthony.mediadatabase.season.Season;
import com.anthony.mediadatabase.season.SeasonRepository;
import com.anthony.mediadatabase.user.User;
import com.anthony.mediadatabase.user.UserAuthenticatedController;

@RestController
public class TVShowRestController extends UserAuthenticatedController {

	@Autowired
	SeasonRepository seasonRepository;

	@PostMapping("/tvshows/season/set")
	public ResponseEntity<?> tvShowSetSeason(@RequestBody Map<String, Long> ajaxRequestBody) {
		User user = getUser();
		AjaxResponseBody result = new AjaxResponseBody();

		Season season = seasonRepository.findByUserAndSeasonId(user.getId(), ajaxRequestBody.get("userSeasonId"));
		if (season == null) {
			result.setMsg("Season not found!");
			return ResponseEntity.badRequest().body(result);
		}
		season.getTvShow().setCurrentSeason(season.getSeasonNum());
		seasonRepository.save(season);
		result.setMsg("success;" + season.getSeasonNum() + ";" + season.getEpisode().toString());
		return ResponseEntity.ok(result);
	}
}
