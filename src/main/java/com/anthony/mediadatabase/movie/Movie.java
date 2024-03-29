package com.anthony.mediadatabase.movie;

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
@Table(name = "movie")
public class Movie extends MediaItemProperties {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long movieId;

	private Long userMovieId;
	@Enumerated(EnumType.ORDINAL)
	private MovieStatus status;
	private Integer length;

	/**
	 * Initialize a default Movie
	 */
	public Movie() {
		this("", 0, "", false, "ToWatch", 0);
	}

	/**
	 * Initialize a movie with passed values
	 * 
	 * @param name       - String name of this movie
	 * @param rating     - Integer rating of this movie
	 * @param genre      - String genre of this movie
	 * @param isFavorite - boolean true if this movie is a favorite, false otherwise
	 * @param watching   - String status of this movie
	 * @param length     - Integer length of this movie in minutes
	 */
	public Movie(String name, Integer rating, String genre, boolean isFavorite, String watching, Integer length) {
		mediaItem = new MediaItem(name, rating, genre, isFavorite);
		setWatching(watching);
		setLength(length);
	}

	/**
	 * Updated this movie's user editable values to match the updated movie's values
	 * 
	 * @param updatedMovie - Movie that holds the user edited values of this movie
	 */
	public void updateMovie(Movie updatedMovie) {
		setName(updatedMovie.getName());
		setRating(updatedMovie.getRating());
		setGenre(updatedMovie.getGenre());
		setWatching(updatedMovie.getWatching());
		setLength(updatedMovie.getLength());
		setIsFavorite(updatedMovie.getIsFavorite());
	}

	public Long getUserMovieId() {
		return userMovieId;
	}

	public void setUserMovieId(Long userMovieId) {
		this.userMovieId = userMovieId;
	}

	/**
	 * Get this movies status
	 * 
	 * @return String representing this movies status
	 */
	public String getWatching() {
		return status.toString();
	}

	/**
	 * Set this movies watching status
	 * 
	 * @param String watching status of this movie
	 */
	public void setWatching(String watching) {
		if (watching.equals(MovieStatus.Watching.toString())) {
			setStatus(MovieStatus.Watching);
		} else if (watching.equals(MovieStatus.Watched.toString())) {
			setStatus(MovieStatus.Watched);
		} else {
			setStatus(MovieStatus.ToWatch);
		}
	}

	// Set this movies MovieStatus
	private void setStatus(MovieStatus status) {
		this.status = status;
	}

	/**
	 * Get the MovieStatus of this movie
	 * 
	 * @return MovieStatus representing this movies status
	 */
	public MovieStatus getStatus() {
		return status;
	}

	/**
	 * Get this movies Id
	 * 
	 * @return Long representing this movies movieId
	 */
	public Long getMovieId() {
		return movieId;
	}

	/**
	 * Set this movies movieId
	 * 
	 * @param id - Long id to set this movies movieId to
	 */
	public void setMovieId(Long id) {
		movieId = id;
	}

	/**
	 * Get the length of this movie
	 * 
	 * @return Integer representing the length of this movie
	 */
	public Integer getLength() {
		return length;
	}

	/**
	 * Set the length of this movie
	 * 
	 * @param length - Integer length of this movie in minutes
	 */
	public void setLength(Integer length) {
		this.length = length;
	}

	enum MovieStatus {
		Watching, ToWatch, Watched
	}

}
