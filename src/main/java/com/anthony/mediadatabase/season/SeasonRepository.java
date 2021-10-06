package com.anthony.mediadatabase.season;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SeasonRepository extends CrudRepository<Season, Long> {
	Season findById(long id);

	@Query("SELECT s FROM Season AS s WHERE s.tvShow.mediaItem.user.id = ?1 AND s.userSeasonId = ?2")
	Season findByUserAndSeasonId(Long userId, Long userSeasonId);

	@Query("SELECT s FROM Season AS s WHERE s.tvShow.mediaItem.user.id = ?1 AND s.tvShow.id = ?2 AND s.seasonNum = ?3")
	Season[] findDuplicateSeason(Long userId, Long tvShowId, Integer seasonNum);

	@Query("SELECT MAX(userSeasonId) FROM Season AS s WHERE s.tvShow.mediaItem.user.id = ?1")
	Long findLatestUserSeasonId(Long userId);
}
