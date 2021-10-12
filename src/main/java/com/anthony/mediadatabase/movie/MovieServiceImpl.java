package com.anthony.mediadatabase.movie;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anthony.mediadatabase.movie.Movie.MovieStatus;
import com.anthony.mediadatabase.user.User;

@Service
public class MovieServiceImpl implements MovieService {

	@Autowired
	MovieRepository movieRepository;

	@Override
	public void save(Movie movie) {
		if (movie.getUserMovieId() == null) {
			movie.setUserMovieId(getNextUserMovieId(movie.getUser()));
		}
		movieRepository.save(movie);
	}

	@Override
	public List<Movie> findAll(Long userId) {
		return movieRepository.findAll(userId);
	}

	@Override
	public List<Movie> findByIsFavorite(Long userId) {
		return movieRepository.findByIsFavorite(userId);
	}

	@Override
	public List<Movie> findByStatus(Long userId, MovieStatus status) {
		return movieRepository.findByStatus(userId, status);
	}

	@Override
	public Movie findByUserMovieId(Long userId, Long movieId) {
		return movieRepository.findByUserMovieId(userId, movieId);
	}

	@Override
	public void delete(Movie movie) {
		movieRepository.delete(movie);
	}

	// Get the next userMovieId for a new movie
	private long getNextUserMovieId(User user) {
		Long latestId = movieRepository.findLatestUserMovieId(user.getId());
		if (latestId != null)
			return latestId + 1;
		return 1L;
	}
}
