package com.anthony.mediadatabase.repository;

import org.springframework.data.repository.CrudRepository;

import com.anthony.mediadatabase.model.MediaItem;


public interface MediaRepository extends CrudRepository<MediaItem, Long>{
	MediaItem findById(long id);

}
