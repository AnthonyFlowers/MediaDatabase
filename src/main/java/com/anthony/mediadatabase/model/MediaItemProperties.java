package com.anthony.mediadatabase.model;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
public abstract class MediaItemProperties {
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "media_id", referencedColumnName = "media_id", nullable = false)
	protected MediaItem mediaItem;
	
	public User getUser() {
		return mediaItem.getUser();
	}
	
	public void setUser(User user) {
		mediaItem.setUser(user);
	}

	public Long getMediaId() {
		return mediaItem.getMediaId();
	}

	public void setMediaId(Long id) {
		mediaItem.setId(id);
	}

	public String getName() {
		return mediaItem.getName();
	}

	public void setName(String name) {
		mediaItem.setName(name);
	}

	public Integer getRating() {
		return mediaItem.getRating();
	}

	public void setRating(Integer rating) {
		mediaItem.setRating(rating);
	}

	public String getGenre() {
		return mediaItem.getGenre();
	}

	public void setGenre(String genre) {
		mediaItem.setGenre(genre);
	}

	public boolean getIsFavorite() {
		return mediaItem.getIsFavorite();
	}

	public void setIsFavorite(boolean isFavorite) {
		mediaItem.setIsFavorite(isFavorite);
	}
}
