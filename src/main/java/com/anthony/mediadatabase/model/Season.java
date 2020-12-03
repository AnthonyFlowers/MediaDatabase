package com.anthony.mediadatabase.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "seasons")
public class Season implements Comparable<Season>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "season_id")
	private Long seasonId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tvshow_id", referencedColumnName = "tvshow_id")
	private TVShow tvShow;

	private Integer episode;
	private Integer seasonNum;

	public Season() {
	}

	public Season(Integer index) {
		setSeasonNum(index);
		setEpisode(0);
	}

	public Season(TVShow show, Integer season, Integer episode) {
		this.seasonNum = season;
		this.tvShow = show;
		this.episode = episode;
	}

	public Long getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(Long seasonId) {
		this.seasonId = seasonId;
	}

	public TVShow getTvShow() {
		return tvShow;
	}

	public void setTvShow(TVShow tvShow) {
		this.tvShow = tvShow;
	}

	public Integer getSeasonNum() {
		return seasonNum;
	}

	public void setSeasonNum(Integer seasonNum) {
		this.seasonNum = seasonNum;
	}

	public Integer getEpisode() {
		return episode;
	}

	public void setEpisode(Integer episode) {
		this.episode = episode;
	}

	@Override
	public int compareTo(Season o) {
		return seasonNum - o.getSeasonNum();
	}
}