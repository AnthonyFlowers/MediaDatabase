package com.anthony.mediadatabase.tvshow;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface TVShowRepository extends CrudRepository<TVShow, Long> {

	@Query("SELECT s FROM TVShow AS s WHERE s.mediaItem.user.id = ?1 AND s.userShowId = ?2")
	TVShow findByUserShowId(Long userId, Long userShowId);

	@Query("SELECT MAX(userShowId) FROM TVShow AS s WHERE s.mediaItem.user.id = ?1")
	Long findLatestUserShowId(Long userId);

	@Query("SELECT s FROM TVShow as s WHERE s.mediaItem.user.id = ?1")
	List<TVShow> findAllByUserId(Long userId);

	@Query("SELECT s FROM TVShow as s WHERE s.mediaItem.isFavorite = 1 AND s.mediaItem.user.id = ?1")
	List<TVShow> findByIsFavorite(Long userId);

	@Query("SELECT s FROM TVShow as s WHERE s.status = 0 AND s.mediaItem.user.id = ?1")
	List<TVShow> findByStatusWatching(Long userId);

	@Query("SELECT s FROM TVShow as s WHERE s.status = 1 AND s.mediaItem.user.id = ?1")
	List<TVShow> findByStatusToWatch(Long userId);

	@Query("SELECT s FROM TVShow as s WHERE s.status = 2 AND s.mediaItem.user.id = ?1")
	List<TVShow> findByStatusWatched(Long userId);
}
