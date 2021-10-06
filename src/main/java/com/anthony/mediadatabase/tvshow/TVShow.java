package com.anthony.mediadatabase.tvshow;

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

import com.anthony.mediadatabase.media.MediaItem;
import com.anthony.mediadatabase.media.MediaItemProperties;
import com.anthony.mediadatabase.season.Season;

@Entity
@Table(name = "tvshow")
public class TVShow extends MediaItemProperties {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tvshow_id")
	private Long tvShowId;

	@OneToMany(mappedBy = "tvShow", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Season> seasons = new ArrayList<Season>();

	private Long userShowId;
	private Integer currentSeason;
	private TVStatus status;

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
	 * @param Integer season to set this TVShow's currentSeason to
	 */
	public void setCurrentSeason(Integer season) {
		this.currentSeason = season;
	}

	public void setEpisode(Integer season, Integer episode) {
		for (Season s : seasons) {
			if (s.getSeasonNum() == season) {
				s.setEpisode(episode);
			}
		}
	}

	public Integer getEpisode() {
		if (getCurrentSeasonItem() == null) {
			return 0;
		}
		return getCurrentSeasonItem().getEpisode();
	}

	/**
	 * Get the current Season
	 * 
	 * @return The current Season
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
	 * @return TVStatus status of this TVShow
	 */
	public String getWatching() {
		return status.toString();
	}

	/**
	 * Add a new season to this TVShow
	 * 
	 * @param season - Season to add to this TVShow
	 * @return Season that was added to this TVShow
	 */
	public Season addSeason(Season season) {
		season.setTvShow(this);
		seasons.add(season);
		return season;
	}

	/**
	 * Set this TVShows status
	 * 
	 * @param TVStatus status to set this TVShow's watching status to
	 */
	private void setStatus(TVStatus status) {
		this.status = status;
	}

	/**
	 * Set this TVShow's tvShowId
	 * 
	 * @param Long tvShowId to set this TVShow's tvShowId to
	 */
	public void setTvShowId(Long tvShowId) {
		this.tvShowId = tvShowId;
	}

	/**
	 * Set this TVShow's watching status
	 * 
	 * @param String watching to set this TVShow's watching status to
	 */
	public void setWatching(String watching) {
		if (watching.equals("Watching")) {
			setStatus(TVStatus.Watching);
		} else if (watching.equals("Watched")) {
			setStatus(TVStatus.Watched);
		} else {
			setStatus(TVStatus.ToWatch);
		}
	}

	/**
	 * Update this TVShow's values to match the passed TVShow's values
	 * 
	 * @param TVShow show to set this TVShow's values to
	 */
	public void update(TVShow show) {
		setName(show.getName());
		setRating(show.getRating());
		setGenre(show.getGenre());
		setStatus(show.status);
		setIsFavorite(show.getIsFavorite());
	}

	/**
	 * Get this TVShow's userShowId
	 * 
	 * @return Long this TVShow's userShowId
	 */
	public Long getUserShowId() {
		return userShowId;
	}

	/**
	 * 
	 * @param userShowId
	 */
	public void setUserShowId(Long userShowId) {
		this.userShowId = userShowId;
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
	Watching, ToWatch, Watched
}
