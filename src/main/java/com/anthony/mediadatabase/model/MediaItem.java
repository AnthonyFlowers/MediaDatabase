package com.anthony.mediadatabase.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "media_item")
public class MediaItem {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "media_id")
	private Long mediaId;
	private String name;
	private Integer rating;
	private String genre;
	private boolean isFavorite;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
	private User user;
	


	/**
	 * Initialize a media item with empty values
	 */
	public MediaItem() {
		this("", 0, "", false);
	}

	/**
	 * Initialize a media item with passed values
	 * 
	 * @param name       - String representing the name of this media item
	 * @param rating     - Integer representing the rating of this media item
	 * @param genre      - String representing the genre of this media item
	 * @param isFavorite - boolean true if this media item is a favorite, false
	 *                   otherwise
	 */
	public MediaItem(String name, Integer rating, String genre, boolean isFavorite) {
		setName(name);
		setRating(rating);
		setGenre(genre);
		setIsFavorite(isFavorite);
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Get the favorite status of this media item
	 * 
	 * @return boolean - true if this media item is a favorite, false otherwise
	 */
	public boolean getIsFavorite() {
		return isFavorite;
	}

	/**
	 * Set the favorite status of this media item
	 * 
	 * @param isFavorite - boolean true if this media item is a favorite, false
	 *                   otherwise
	 */
	public void setIsFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	/**
	 * Set the mediaId of this item
	 * 
	 * @param id - Long representing the id of this media item
	 */
	public void setId(Long id) {
		this.mediaId = id;
	}

	/**
	 * Set this media items name
	 * 
	 * @param name - String representing the name of this media item
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Set this media items rating
	 * 
	 * @param rating - Integer representing the rating of this media item
	 */
	public void setRating(Integer rating) {
		this.rating = rating;
	}

	/**
	 * Set this media items genre
	 * 
	 * @param genre - String representing a media item genre
	 */
	public void setGenre(String genre) {
		this.genre = genre;
	}

	/**
	 * Get this media items id
	 * 
	 * @return Long representing this media items id
	 */
	public Long getMediaId() {
		return mediaId;
	}

	/**
	 * Get this media items name
	 * 
	 * @return String representing this media items name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get this media items rating
	 * 
	 * @return Integer representing this media items rating
	 */
	public Integer getRating() {
		return rating;
	}

	/**
	 * Get this media items genre
	 * 
	 * @return String representing this media items genre
	 */
	public String getGenre() {
		return genre;
	}

	@Override
	public String toString() {
		String formatter = "Movie[id=%d, name='%s', rating=%d, genre='%s']";
		return String.format(formatter, mediaId, name, rating, genre);
	}
}
