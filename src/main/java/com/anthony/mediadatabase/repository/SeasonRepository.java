package com.anthony.mediadatabase.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.anthony.mediadatabase.model.Season;

public interface SeasonRepository extends CrudRepository<Season, Long>{
	Season findById(long id);
	
	@Query("SELECT s FROM Season AS s WHERE s.tvShow.mediaItem.user.id = ?1 AND s.userSeasonId = ?2")
	Season findByUserAndSeasonId(Long userId, Long userSeasonId);
	
	@Query("SELECT MAX(userSeasonId) FROM Season AS s WHERE s.tvShow.mediaItem.user.id = ?1")
	Long findLatestUserSeasonId(Long userId);
}
