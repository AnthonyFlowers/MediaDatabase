package com.anthony.mediadatabase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.anthony.mediadatabase.model.Movie;

@Transactional(readOnly = true)
public interface MovieRepository extends CrudRepository<Movie, Long> {

	@Query("SELECT m FROM Movie AS m WHERE m.mediaItem.user.id = ?1 AND m.userMovieId = ?2")
	Movie findByUserMovieId(Long userId, Long userMovieId);

	@Query("SELECT MAX(userMovieId) FROM Movie AS m WHERE m.mediaItem.user.id = ?1")
	Long findLatestUserMovieId(Long userId);

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
