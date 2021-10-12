package com.anthony.mediadatabase.book;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return BookValidator.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Book book = (Book) target;
		try {
			BookStatus.valueOf(book.getStatus());
		} catch(IllegalArgumentException e) {
			errors.reject("status", "Value.book.status");
		}
	}

}
