package com.anthony.mediadatabase.book;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anthony.mediadatabase.user.User;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	BookRepository bookRepository;

	@Override
	public void save(Book book) {
		if(book.getUserBookId() == null) {
			book.setUserBookId(getNextUserBookId(book.getUser()));
		}
		bookRepository.save(book);
	}

	@Override
	public List<Book> findAll(Long userId) {
		return bookRepository.findAll(userId);
	}

	@Override
	public List<Book> findByIsFavorite(Long userId) {
		return bookRepository.findByIsFavorite(userId);
	}

	@Override
	public List<Book> findByUserStatus(Long userId, BookStatus status) {
		return bookRepository.findByUserStatus(userId, status);
	}

	@Override
	public Book findByUserBookId(Long userId, Long bookId) {
		return bookRepository.findByUserBookId(userId, bookId);
	}

	@Override
	public void delete(Book bookToDelete) {
		bookRepository.delete(bookToDelete);
	}

	// Get the next userBookId for a new Book
	private long getNextUserBookId(User user) {
		Long latestId = bookRepository.findLatestUserBookId(user.getId());
		if (latestId != null)
			return latestId + 1;
		return 1L;
	}

}
