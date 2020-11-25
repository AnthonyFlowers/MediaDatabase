package com.anthony.moviedatabase.model.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.anthony.mediadatabase.model.MediaItem;

class TestMediaItem {
	
	@Test
	void testUpdateMediaItem() {
		MediaItem mediaItem = new MediaItem();
		mediaItem.setName("My Hero Academia");
		mediaItem.setRating(5);
		mediaItem.setGenre("Action");
		mediaItem.setIsFavorite(true);
		assertEquals("My Hero Academia", mediaItem.getName());
		assertEquals(5, mediaItem.getRating());
		assertEquals("Action", mediaItem.getGenre());
		assertEquals(true, mediaItem.getIsFavorite());
	}

	@Test
	void testMediaItem() {
		MediaItem mediaItem = new MediaItem();
		assertEquals("", mediaItem.getName());
		assertEquals(0, mediaItem.getRating());
		assertEquals("", mediaItem.getGenre());
		assertEquals(false, mediaItem.getIsFavorite());
		
		mediaItem = exampleMediaItem();
		assertEquals("Media Ex", mediaItem.getName());
		assertEquals(5, mediaItem.getRating());
		assertEquals("Genre Ex", mediaItem.getGenre());
		assertEquals(true, mediaItem.getIsFavorite());
	}
	
	MediaItem exampleMediaItem() {
		return new MediaItem("Media Ex", 5, "Genre Ex", true);
	}

}
