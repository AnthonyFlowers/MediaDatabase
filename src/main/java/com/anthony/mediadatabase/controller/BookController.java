package com.anthony.mediadatabase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.anthony.mediadatabase.model.Book;
import com.anthony.mediadatabase.model.User;
import com.anthony.mediadatabase.repository.BookRepository;

@Controller
public class BookController extends UserAuthenticatedController {
	@Autowired
	private BookRepository bookRepository;

	/**
	 * Mapping for the Book list page
	 * 
	 * @return sends control to the Book list page with all Books included
	 */
	@GetMapping("/books")
	public String BookPage(Model model) {
		model.addAttribute("books", bookRepository.findAll(getUser().getId()));
		return "books";
	}

	/**
	 * Mapping for loading the page to add a new Book
	 * 
	 * @return sends control to the new Book page
	 */
	@GetMapping("/books/new")
	public String newBookForm(Model model) {
		model.addAttribute("book", new Book());
		return "bookNew";
	}

	/**
	 * Mapping for adding a new Book to the DB
	 * 
	 * @param book - parameter that holds the new Book to add
	 * @return sends control to the Book result page of the new Book
	 */
	@PostMapping("/books/new")
	public String newBook(@ModelAttribute Book book, Model model) {
		Book newBook = new Book();
		User user = getUser();
		Long nextUserBookId = getNextUserBookId(user);
		newBook.updateBook(book);
		newBook.setUser(user);
		newBook.setUserBookId(nextUserBookId);
		bookRepository.save(newBook);
		model.addAttribute("book", newBook);
		return "bookResult";
	}

	/**
	 * Mapping for the Book favorites list page
	 * 
	 * @return sends control to the Book list page with Books marked as "Favorite"
	 */
	@GetMapping("/books/favorites")
	public String getFavoriteBooks(Model model) {
		model.addAttribute("books", bookRepository.findByIsFavorite(getUser().getId()));
		return "books";
	}

	/**
	 * Mapping for the Book reading list page
	 * 
	 * @return sends control to the Book list page with Books marked as "Reading"
	 */
	@GetMapping("/books/reading")
	public String getBooksBeingread(Model model) {
		model.addAttribute("books", bookRepository.findByStatusReading(getUser().getId()));
		return "books";
	}

	/**
	 * Mapping for the book read list page
	 * 
	 * @return sends control to the Book list page with Books marked as "Read"
	 */
	@GetMapping("/books/read")
	public String getBooksread(Model model) {
		model.addAttribute("books", bookRepository.findByStatusRead(getUser().getId()));
		return "books";
	}

	/**
	 * Mapping for the book to read list page
	 * 
	 * @return sends control to the Book list page with Books marked as "To-Read"
	 */
	@GetMapping("/books/toread")
	public String getBooksToWatch(Model model) {
		model.addAttribute("books", bookRepository.findByStatusToRead(getUser().getId()));
		return "books";
	}

	/**
	 * Mapping for loading the edit book page
	 * 
	 * @param bookId - parameter that holds the id of the book to edit
	 * @return sends control to the Book edit page of the selected Book or
	 *         redirects to the Book list page if the Book does not exist
	 */
	@GetMapping("/books/edit")
	public String editBook(@RequestParam("bookId") Long bookId, Model model) {
		User user = getUser();
		Book selectedBook = bookRepository.findByUserBookId(user.getId(), bookId);
		if (selectedBook != null) {
			model.addAttribute("book", selectedBook);
			return "bookEdit";
		} else {
			model.addAttribute("errorBookId", "Could not find the Book with that id for the current user.");
		}
		return "redirect:/books";
	}

	/**
	 * Mapping for editing a Book
	 * 
	 * @param book - holds the edited Book
	 * @return sends control to the Book result page of the edited Book
	 */
	@PostMapping("/books/edit")
	public String updateMove(@ModelAttribute Book book, Model model) {
		User user = getUser();
		Book selectedBook = bookRepository.findByUserBookId(user.getId(), book.getUserBookId());
		if (selectedBook != null) {
			selectedBook.updateBook(book);
			bookRepository.save(selectedBook);
			return "bookResult";
		} else {
			model.addAttribute("errorBookId", "Could not find the Book with that id for the current user.");
		}
		return "redirect:/books";
	}

	/**
	 * Mapping for loading the delete Book page
	 * 
	 * @param bookId - holds the id of the book to delete
	 * @return sends control to the delete book page or redirects to the Book list
	 *         page if the Book does not exist
	 */
	@GetMapping("/books/delete")
	public String deleteBookForm(@RequestParam("selectedBook") Long bookId, Model model) {
		User user = getUser();
		Book selectedBook = bookRepository.findByUserBookId(user.getId(), bookId);
		if (selectedBook != null) {
			model.addAttribute("book", selectedBook);
			return "bookDelete";
		} else {
			model.addAttribute("errorBookId", "Could not find the Book with that id for the current user.");
		}
		return "redirect:/books";
	}

	/**
	 * Mapping for deleting a Book
	 * 
	 * @param BookId - holds the id of the Book to delete
	 * @return redirects to the Book list page
	 */
	@PostMapping("/books/delete/confirm")
	public String deleteBook(@ModelAttribute("book") Book book, Model model) {
		User user = getUser();
		Book BookToDelete = bookRepository.findByUserBookId(user.getId(), book.getUserBookId());
		bookRepository.delete(BookToDelete);
		model.addAttribute("infoDeleteSuccess", "Book deletion successful.");
		return "redirect:/books";
	}

	// Get the next userBookId for a new Book
	private long getNextUserBookId(User user) {
		Long latestId = bookRepository.findLatestUserBookId(user.getId());
		if (latestId != null)
			return latestId + 1;
		return 1L;
	}
}
