package com.anthony.mediadatabase.book;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.anthony.mediadatabase.media.MediaItem;
import com.anthony.mediadatabase.media.MediaItemProperties;

@Entity
@Table(name = "book")
public class Book extends MediaItemProperties {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookId;

	private Long userBookId;
	@Enumerated(EnumType.ORDINAL)
	private BookStatus status;
	private Integer currentPage;
	private Integer totalPages;

	/**
	 * Initialize a default Book
	 */
	public Book() {
		this("", 0, "", false, "ToRead", 0, 0);
	}

	public Book(String name, Integer rating, String genre, boolean isFavorite, String readingStatus, Integer length,
			Integer page) {
		mediaItem = new MediaItem(name, rating, genre, isFavorite);
		setStatus(readingStatus);
		setTotalPages(length);
		setCurrentPage(page);
	}

	public void updateBook(Book updatedBook) {
		setName(updatedBook.getName());
		setRating(updatedBook.getRating());
		setGenre(updatedBook.getGenre());
		setStatus(updatedBook.getStatus());
		setTotalPages(updatedBook.getTotalPages());
		setCurrentPage(updatedBook.getCurrentPage());
		setIsFavorite(updatedBook.getIsFavorite());
	}

	public Long getBookId() {
		return bookId;
	}

	public Long getUserBookId() {
		return userBookId;
	}

	public void setUserBookId(Long userBookId) {
		this.userBookId = userBookId;
	}

	public String getStatus() {
		return status.toString();
	}

	public void setStatus(String status) {
		for (BookStatus s : BookStatus.values()) {
			if (s.toString().equals(status)) {
				setStatus(s);
			}
		}
	}

	private void setStatus(BookStatus status) {
		this.status = status;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

}

enum BookStatus {
	Reading, ToRead, Read
}
