package com.anthony.mediadatabase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.anthony.mediadatabase.model.TVShow;

public interface TVShowRepository extends CrudRepository<TVShow, Long>{
	TVShow findById(long id);

	@Query("SELECT s FROM TVShow as s WHERE s.mediaItem.isFavorite = 1")
	List<TVShow> findByIsFavorite();

	@Query("SELECT s FROM TVShow as s WHERE s.status = 0")
	List<TVShow> findByStatusWatching();
	
	@Query("SELECT s FROM TVShow as s WHERE s.status = 1")
	List<TVShow> findByStatusToWatch();
	
	@Query("SELECT s FROM TVShow as s WHERE s.status = 2")
	List<TVShow> findByStatusWatched();
}
