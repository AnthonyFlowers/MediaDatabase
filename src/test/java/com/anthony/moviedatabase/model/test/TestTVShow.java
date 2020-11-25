package com.anthony.moviedatabase.model.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.anthony.mediadatabase.model.TVShow;

class TestTVShow {
	
	@Test
	void testTVShowSeasons() {
		TVShow show = new TVShow();
		assertEquals(0, show.getEpisode());
		show = exampleTVShow();
		assertEquals(0, show.getEpisode());
	}

	@Test
	void testUpdatingShow() {
		// Test changing seasons
		TVShow show = new TVShow();
		assertEquals(0, show.getSeasons().size());
		show.addSeason();
		assertEquals(0, show.getCurrentSeasonItem().getSeasonNum());
		assertEquals(1, show.getSeasons().size());
		show.addSeason();
		show.setCurrentSeason(1);
		assertEquals(2, show.getSeasons().size());
		assertEquals(1, show.getCurrentSeasonItem().getSeasonNum());
		show = exampleTVShow();
		// Test changing status
		assertEquals("Watched", show.getWatching());
		show.setWatching("ToWatch");
		assertEquals("ToWatch", show.getWatching());
		show.setWatching("Watching");
		assertEquals("Watching", show.getWatching());
		// Test updating current season
		assertEquals(0, show.getCurrentSeason());
		show.setCurrentSeason(8);
		assertEquals(8, show.getCurrentSeason());
		show.setCurrentSeason(3);
		assertEquals(3, show.getCurrentSeason());
	}

	@Test
	void testTVShow(){
		// Test initializing a tv show
		TVShow show = new TVShow();
		assertEquals("", show.getName());
		assertEquals(0, show.getRating());
		assertEquals("", show.getGenre());
		assertEquals(false, show.getIsFavorite());
		assertEquals("ToWatch", show.getWatching());
		assertEquals(0, show.getSeasons().size());
		assertEquals(0, show.getCurrentSeason());
		// Test with example tv show with info
		show = exampleTVShow();
		assertEquals("Dexter", show.getName());
		assertEquals(5, show.getRating());
		assertEquals("Detective", show.getGenre());
		assertEquals(true, show.getIsFavorite());
		assertEquals("Watched", show.getWatching());
		assertEquals(8, show.getSeasons().size());
		assertEquals(0, show.getCurrentSeason());
	}

	@Test
	void testTVShowExceptions() {
		TVShow tvShow = exampleTVShow();
		assertThrows(IllegalArgumentException.class, () -> {
			tvShow.setCurrentSeason(9);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			tvShow.setCurrentSeason(-1);
		});
	}

	TVShow exampleTVShow() {
		return new TVShow("Dexter", 5, "Detective", true, "Watched", 8);
	}

}
