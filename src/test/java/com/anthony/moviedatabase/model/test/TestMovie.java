package com.anthony.moviedatabase.model.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.anthony.mediadatabase.model.Movie;

class TestMovie {

	@Test
	void testUpdateMovie() {
		Movie movie = new Movie();
		movie.setName("Ant-Man");
		movie.setRating(4);
		movie.setGenre("Action");
		movie.setIsFavorite(false);
		movie.setWatching("Watching");
		movie.setLength(117);
		assertEquals("Ant-Man", movie.getName());
		assertEquals(4, movie.getRating());
		assertEquals("Action", movie.getGenre());
		assertEquals(false, movie.getIsFavorite());
		assertEquals("Watching", movie.getWatching());
		assertEquals(117, movie.getLength());
	}

	@Test
	void testMovie() {
		Movie movie = new Movie();
		assertEquals("", movie.getName());
		assertEquals(0, movie.getRating());
		assertEquals("", movie.getGenre());
		assertEquals(false, movie.getIsFavorite());
		assertEquals("ToWatch", movie.getWatching());
		assertEquals(0, movie.getLength());
		movie = exampleMovie();
		assertEquals("Iron Man", movie.getName());
		assertEquals(5, movie.getRating());
		assertEquals("Action", movie.getGenre());
		assertEquals(true, movie.getIsFavorite());
		assertEquals("Watched", movie.getWatching());
		assertEquals(126, movie.getLength());
	}

	Movie exampleMovie() {
		return new Movie("Iron Man", 5, "Action", true, "Watched", 126);
	}

}
