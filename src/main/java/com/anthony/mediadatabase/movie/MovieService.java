package com.anthony.mediadatabase.movie;

import java.util.List;

import com.anthony.mediadatabase.movie.Movie.MovieStatus;

public interface MovieService {

	void save(Movie movie);

	List<Movie> findAll(Long userId);

	List<Movie> findByIsFavorite(Long userId);

	List<Movie> findByStatus(Long userId, MovieStatus status);

	Movie findByUserMovieId(Long userId, Long movieId);

	void delete(Movie movie);

}
