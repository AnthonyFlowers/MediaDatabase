package com.anthony.mediadatabase.media;

import org.springframework.data.repository.CrudRepository;

public interface MediaRepository extends CrudRepository<MediaItem, Long> {
	MediaItem findById(long id);

}
