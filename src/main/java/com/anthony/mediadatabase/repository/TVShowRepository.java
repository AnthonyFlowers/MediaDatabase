package com.anthony.mediadatabase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.anthony.mediadatabase.model.TVShow;

public interface TVShowRepository extends CrudRepository<TVShow, Long>{
	TVShow findById(long id);
	
	@Query("SELECT s FROM TVShow as s WHERE s.mediaItem.user.id = ?1")
	List<TVShow> findAll(Long userId);

	@Query("SELECT s FROM TVShow as s WHERE s.mediaItem.isFavorite = 1 AND s.mediaItem.user.id = ?1")
	List<TVShow> findByIsFavorite(Long userId);

	@Query("SELECT s FROM TVShow as s WHERE s.status = 0 AND s.mediaItem.user.id = ?1")
	List<TVShow> findByStatusWatching(Long userId);
	
	@Query("SELECT s FROM TVShow as s WHERE s.status = 1 AND s.mediaItem.user.id = ?1")
	List<TVShow> findByStatusToWatch(Long userId);
	
	@Query("SELECT s FROM TVShow as s WHERE s.status = 2 AND s.mediaItem.user.id = ?1")
	List<TVShow> findByStatusWatched(Long userId);
}
