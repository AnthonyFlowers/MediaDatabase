package com.anthony.mediadatabase.media;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class MediaItemValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return MediaItemValidator.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		MediaItem mediaItem = (MediaItem) target;
		if (mediaItem.getName().length() > 255) {
			errors.rejectValue("name", "Size.mediaItem.name");
		}
		if (mediaItem.getGenre().length() > 255) {
			errors.rejectValue("genre", "Size.mediaItem.genre");
		}
	}

}
