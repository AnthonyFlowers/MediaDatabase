package com.anthony.mediadatabase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.anthony.mediadatabase.model.Movie;
import com.anthony.mediadatabase.model.User;

public interface MovieRepository extends CrudRepository<Movie, Long> {

	Movie findById(long id);
	
	@Query("SELECT m FROM Movie as m WHERE m.mediaItem.user.id = ?1")
	List<Movie> findAll(Long userId);

	@Query("SELECT m FROM Movie as m WHERE m.mediaItem.isFavorite = 1 AND m.mediaItem.user.id = ?1")
	List<Movie> findByIsFavorite(Long userId);

	@Query("SELECT m FROM Movie as m WHERE m.status = 0  AND m.mediaItem.user.id = ?1")
	List<Movie> findByStatusWatching(Long userId);
	
	@Query("SELECT m FROM Movie as m WHERE m.status = 1  AND m.mediaItem.user.id = ?1")
	List<Movie> findByStatusToWatch(Long userId);
	
	@Query("SELECT m FROM Movie as m WHERE m.status = 2  AND m.mediaItem.user.id = ?1")
	List<Movie> findByStatusWatched(Long userId);
}
