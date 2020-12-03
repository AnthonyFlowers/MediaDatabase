package com.anthony.mediadatabase.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tvshow")
public class TVShow extends MediaItemProperties {

	@OneToMany(mappedBy = "tvShow", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Season> seasons = new ArrayList<Season>();
	private Integer currentSeason;
	private TVStatus status;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "tvshow_id")
	private Long tvShowId;

	/**
	 * Initialize a show with empty values
	 */
	public TVShow() {
		this("", 0, "", false, "", 0);
	}

	/**
	 * Initialize a show with passed values
	 * 
	 * @param name        - String name of the show
	 * @param rating      - Integer rating of the show
	 * @param genre       - String genre of the show
	 * @param isFavorite  - boolean true if this show is a favorite false otherwise
	 * @param watching    - String status of this show
	 * @param seasonCount - Integer amount of seasons to initialize with
	 */
	public TVShow(String name, Integer rating, String genre, boolean isFavorite, String watching, Integer seasonCount) {
		mediaItem = new MediaItem(name, rating, genre, isFavorite);
		setWatching(watching);
		setCurrentSeason(0);
		addSeasons(seasonCount);
	}

	/**
	 * Get the current season number the user is watching
	 * 
	 * @return An Integer representing the current season
	 */
	public Integer getCurrentSeason() {
		return currentSeason;
	}

	/**
	 * Set the current season
	 * 
	 * @param season
	 * @throws IllegalValueException if <code>season</code> is not a valid value
	 */
	public void setCurrentSeason(Integer season) {
		currentSeasonValidation(season);
		this.currentSeason = season;
	}

	public Integer getEpisode() {
		if(getCurrentSeasonItem() == null) {
			return 0;
		}
		return getCurrentSeasonItem().getEpisode();
	}

	public void setEpisode(Integer episode) {
		getCurrentSeasonItem().setEpisode(episode);
	}

	/**
	 * Get the current Season
	 * 
	 * @return The current <code>Season</code>
	 */
	public Season getCurrentSeasonItem() {
		for (Season s : seasons) {
			if (s.getSeasonNum() == currentSeason) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Get ArrayList of Seasons this show has
	 * 
	 * @return ArrayList of seasons this show has
	 */
	public List<Season> getSeasons() {
		Collections.sort(seasons);
		return seasons;
	}

	/**
	 * Get this shows Id
	 * 
	 * @return Long showId
	 */
	public Long getTvShowId() {
		return tvShowId;
	}

	/**
	 * Get the status of this show
	 * 
	 * @return <link>TVStatus</link> status
	 * @See com.anthony.mediadatabase.model.TVShow.TVStatus
	 */
	public String getWatching() {
		return status.toString();
	}

	// Method to check if a season is valid based on this TVShows season count
	private void currentSeasonValidation(Integer season) {
		if (season > seasons.size()) {
			throw new IllegalArgumentException(
					"Cannot set current season higher than the amount of seasons this show has");
		}
		if (season < 0) {
			throw new IllegalArgumentException("Cannot set current season lower than 0");
		}
	}

	// Add a season for each seasonCount
	private void addSeasons(Integer seasonCount) {
		for (int i = 0; i < seasonCount; i++) {
			addSeason();
		}
	}

	public Season addSeason() {
		Season season = new Season(seasons.size());
		season.setTvShow(this);
		season = addSeason(season);
		return season;
	}
	
	public Season addSeason(Season season) {
		season.setTvShow(this);
		seasons.add(season);
		return season;
	}

	// Set this TVShows status
	private void setStatus(TVStatus status) {
		this.status = status;
	}

	public void setTvShowId(Long tvShowId) {
		this.tvShowId = tvShowId;
	}

	public void setWatching(String watching) {
		if (watching.equals("Watching")) {
			setStatus(TVStatus.Watching);
		} else if (watching.equals("Watched")) {
			setStatus(TVStatus.Watched);
		} else {
			setStatus(TVStatus.ToWatch);
		}
	}
	
	public void update(TVShow show) {
		setName(show.getName());
		setRating(show.getRating());
		setGenre(show.getGenre());
		setStatus(show.status);
		setIsFavorite(show.getIsFavorite());
	}

	@Override
	public String toString() {
		return getName();
	}
}

/**
 * Enumerator representing the status of a TVShow
 * 
 * @author Anthony
 */
enum TVStatus {
	ToWatch, Watched, Watching
}
