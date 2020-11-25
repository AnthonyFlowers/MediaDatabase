package com.anthony.mediadatabase.model;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, Long> {

	Movie findById(long id);

	@Query("SELECT m FROM Movie as m WHERE m.mediaItem.isFavorite = 1")
	List<Movie> findByIsFavorite();

	@Query("SELECT m FROM Movie as m WHERE m.status = 0")
	List<Movie> findByStatusWatching();
	
	@Query("SELECT m FROM Movie as m WHERE m.status = 1")
	List<Movie> findByStatusToWatch();
	
	@Query("SELECT m FROM Movie as m WHERE m.status = 2")
	List<Movie> findByStatusWatched();
}
