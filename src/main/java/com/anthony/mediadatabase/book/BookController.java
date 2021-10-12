package com.anthony.mediadatabase.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anthony.mediadatabase.media.MediaItemValidator;
import com.anthony.mediadatabase.user.User;
import com.anthony.mediadatabase.user.UserAuthenticatedController;

@Controller
public class BookController extends UserAuthenticatedController {
	@Autowired
	private BookService bookService;
	
	@Autowired
	private MediaItemValidator mediaValidator;
	
	@Autowired
	private BookValidator bookValidator;

	/**
	 * Mapping for loading the page to add a new Book
	 */
	@GetMapping("/books/new")
	public String bookNewForm(Model model) {
		model.addAttribute("book", new Book());
		return "book/new";
	}

	/**
	 * Mapping for adding a new Book
	 */
	@PostMapping("/books/new")
	public String bookNewCommit(@ModelAttribute("book") Book book, BindingResult result, Model model) {
		User user = getUser();
		book.setUser(user);
		validateBook(book, result);
		if(result.hasErrors()) {
			return "book/new";
		}
		bookService.save(book);
		model.addAttribute("book", book);
		model.addAttribute("readOnly", true);
		return "book/result";
	}

	/**
	 * Mapping for the Book list page
	 */
	@GetMapping("/books")
	public String bookGetAll(Model model) {
		model.addAttribute("books", bookService.findAll(getUser().getId()));
		return "book/books";
	}

	/**
	 * Mapping for the Book favorites list page
	 */
	@GetMapping("/books/favorites")
	public String bookGetFavorite(Model model) {
		model.addAttribute("books", bookService.findByIsFavorite(getUser().getId()));
		return "book/books";
	}

	/**
	 * Mapping for the Book reading list page
	 */
	@GetMapping("/books/reading")
	public String bookGetReading(Model model) {
		model.addAttribute("books", bookService.findByUserStatus(getUser().getId(), BookStatus.Reading));
		return "book/books";
	}

	/**
	 * Mapping for the book read list page
	 */
	@GetMapping("/books/read")
	public String bookGetRead(Model model) {
		model.addAttribute("books", bookService.findByUserStatus(getUser().getId(), BookStatus.Read));
		return "book/books";
	}

	/**
	 * Mapping for the book to read list page
	 */
	@GetMapping("/books/toread")
	public String bookGetToRead(Model model) {
		model.addAttribute("books", bookService.findByUserStatus(getUser().getId(), BookStatus.ToRead));
		return "book/books";
	}

	/**
	 * Mapping for loading the edit book page
	 */
	@GetMapping("/books/edit")
	public String bookEdit(@RequestParam("bookId") Long bookId, Model model, RedirectAttributes ra) {
		User user = getUser();
		Book selectedBook = bookService.findByUserBookId(user.getId(), bookId);
		if (selectedBook == null) {
			ra.addFlashAttribute("errorNotFound", "Could not find that Book.");
			return "redirect:/books";
		}
		model.addAttribute("book", selectedBook);
		return "book/edit";

	}

	/**
	 * Mapping for editing a Book
	 */
	@PostMapping("/books/edit")
	public String bookEditCommit(@ModelAttribute("book") Book book, BindingResult result, Model model, RedirectAttributes ra) {
		User user = getUser();
		validateBook(book, result);
		if(result.hasErrors()) {
			return "book/edit";
		}
		Book selectedBook = bookService.findByUserBookId(user.getId(), book.getUserBookId());
		if (selectedBook == null) {
			ra.addFlashAttribute("errorNotFound", "Could not find that Book.");
			return "redirect:/books";
		}
		selectedBook.updateBook(book);
		bookService.save(selectedBook);
		model.addAttribute("readOnly", true);
		return "book/result";
	}

	/**
	 * Mapping for loading the delete Book page
	 */
	@GetMapping("/books/delete")
	public String bookDeleteForm(@RequestParam("selectedBook") Long bookId, Model model, RedirectAttributes ra) {
		User user = getUser();
		Book selectedBook = bookService.findByUserBookId(user.getId(), bookId);
		if (selectedBook == null) {
			ra.addFlashAttribute("errorNotFound", "Could not find that Book.");
			return "redirect:/books";
		}
		model.addAttribute("book", selectedBook);
		model.addAttribute("readOnly", true);
		return "book/delete";
	}

	/**
	 * Mapping for deleting a Book
	 */
	@PostMapping("/books/delete/confirm")
	public String bookDeleteCommit(@ModelAttribute("book") Book book, Model model, RedirectAttributes ra) {
		User user = getUser();
		Book bookToDelete = bookService.findByUserBookId(user.getId(), book.getUserBookId());
		model.addAttribute("books", bookService.findAll(getUser().getId()));
		if (bookToDelete == null) {
			ra.addFlashAttribute("errorNotFound", "Could not find that Book.");
			return "redirect:/books";
		}
		bookService.delete(bookToDelete);
		ra.addFlashAttribute("infoSuccess", "Book deletion successful.");
		return "redirect:/books";
	}
	
	private void validateBook(Book book, BindingResult results) {
		mediaValidator.validate(book.getMediaItem(), results);
		bookValidator.validate(book, results);
	}
}
