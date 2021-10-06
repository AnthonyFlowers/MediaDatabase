package com.anthony.mediadatabase.book;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface BookRepository extends CrudRepository<Book, Long> {

	@Query("SELECT b FROM Book AS b WHERE b.mediaItem.user.id = ?1 AND b.userBookId = ?2")
	Book findByUserBookId(Long userId, Long userBookId);

	@Query("SELECT MAX(userBookId) FROM Book AS b WHERE b.mediaItem.user.id = ?1")
	Long findLatestUserBookId(Long userId);

	@Query("SELECT b FROM Book as b WHERE b.mediaItem.user.id = ?1")
	List<Book> findAll(Long userId);

	@Query("SELECT b FROM Book as b WHERE b.mediaItem.isFavorite = 1 AND b.mediaItem.user.id = ?1")
	List<Book> findByIsFavorite(Long userId);

	@Query("SELECT b FROM Book as b WHERE b.status = 0  AND b.mediaItem.user.id = ?1")
	List<Book> findByStatusReading(Long userId);

	@Query("SELECT b FROM Book as b WHERE b.status = 1  AND b.mediaItem.user.id = ?1")
	List<Book> findByStatusToRead(Long userId);

	@Query("SELECT b FROM Book as b WHERE b.status = 2  AND b.mediaItem.user.id = ?1")
	List<Book> findByStatusRead(Long userId);
}
