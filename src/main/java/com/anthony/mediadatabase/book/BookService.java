package com.anthony.mediadatabase.book;

import java.util.List;

public interface BookService {

	void save(Book book);

	List<Book> findAll(Long userId);

	List<Book> findByIsFavorite(Long userId);

	List<Book> findByUserStatus(Long userId, BookStatus status);

	Book findByUserBookId(Long userId, Long bookId);

	void delete(Book bookToDelete);

}
