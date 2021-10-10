package com.anthony.mediadatabase.tvshow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anthony.mediadatabase.season.Season;
import com.anthony.mediadatabase.season.SeasonRepository;
import com.anthony.mediadatabase.user.User;

@Service
public class TVShowServiceImpl implements TVShowService {

	@Autowired
	private TVShowRepository tvShowRepository;

	@Autowired
	private SeasonRepository seasonRepository;

	@Override
	public void save(TVShow tvShow) {
		if(tvShow.getUserShowId() == null) {
			tvShow.setUserShowId(getNextUserShowId(tvShow.getUser()));
		}
		tvShowRepository.save(tvShow);
	}

	@Override
	public List<TVShow> findByUserId(Long userId) {
		return tvShowRepository.findAllByUserId(userId);
	}

	@Override
	public List<TVShow> findByIsFavorite(Long userId) {
		return tvShowRepository.findByIsFavorite(userId);
	}

	@Override
	public List<TVShow> findByStatus(Long userId, int status) {
		return tvShowRepository.findByStatus(userId, status);
	}

	@Override
	public TVShow findByUserShowId(Long userId, Long showId) {
		return tvShowRepository.findByUserShowId(userId, showId);
	}

	@Override
	public boolean addTVShowSeason(User user, Season season, Long tvShowId) {
		TVShow tvShow = findByUserShowId(user.getId(), tvShowId);
		if (!tvShow.hasSeason(season)) {
			season.setUserSeasonId(getNextUserSeasonId(user));
			tvShow.addSeason(season);
			tvShowRepository.save(tvShow);
			return true;
		}
		return false;
	}

	@Override
	public Long deleteSeason(Long userId, Long seasonId) {
		Season season = seasonRepository.findByUserAndSeasonId(userId, seasonId);
		if (season != null) {
			TVShow tvShow = season.getTvShow();
			tvShow.setCurrentSeason(0);
			tvShowRepository.save(tvShow);
			seasonRepository.delete(season);
			return tvShow.getUserShowId();
		}
		return null;
	}

	@Override
	public Season findByUserAndSeasonId(Long userId, Long seasonId) {
		return seasonRepository.findByUserAndSeasonId(userId, seasonId);
	}

	@Override
	public void delete(TVShow showToDelete) {
		tvShowRepository.delete(showToDelete);
	}

	// Get the next userSeasonId for a new season
	private Long getNextUserSeasonId(User user) {
		Long latestSeasonId = seasonRepository.findLatestUserSeasonId(user.getId());
		if (latestSeasonId != null)
			return latestSeasonId + 1;
		return 1L;
	}

	// Get next userShowId for a new TVShow
	private Long getNextUserShowId(User user) {
		Long latestId = tvShowRepository.findLatestUserShowId(user.getId());
		if (latestId != null)
			return latestId + 1;
		return 1L;
	}
}
