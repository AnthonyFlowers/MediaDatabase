package com.anthony.mediadatabase.tvshow;

import java.util.List;

import com.anthony.mediadatabase.season.Season;
import com.anthony.mediadatabase.user.User;

public interface TVShowService {
	void save(TVShow tvShow);

	List<TVShow> findByUserId(Long userId);

	List<TVShow> findByIsFavorite(Long userId);

	List<TVShow> findByStatus(Long userId, TVStatus status);

	TVShow findByUserShowId(Long userId, Long showId);

	boolean addTVShowSeason(User user, Season season, Long tvShowId);

	Long deleteSeason(Long userId, Long seasonId);

	Season findByUserAndSeasonId(Long userId, Long seasonId);

	void delete(TVShow showToDelete);
}
