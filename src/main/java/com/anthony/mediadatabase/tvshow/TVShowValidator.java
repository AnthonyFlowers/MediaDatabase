package com.anthony.mediadatabase.tvshow;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TVShowValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return TVShow.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		TVShow tvShow = (TVShow) target;
		try {
			TVStatus.valueOf(tvShow.getWatching());
		} catch(IllegalArgumentException e) {
			errors.reject("status", "Value.tvShow.status");
		}
	}
}
