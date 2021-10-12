package com.anthony.mediadatabase.movie;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.anthony.mediadatabase.movie.Movie.MovieStatus;

@Component
public class MovieValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Movie.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Movie movie = (Movie) target;
		try {
			MovieStatus.valueOf(movie.getWatching());
		} catch (IllegalArgumentException e) {
			errors.reject("status", "Value.movie.status");
		}
	}

}
