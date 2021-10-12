package com.anthony.mediadatabase.media;

public class AjaxResponseBody {

	private String msg;
	private Integer episodeNum;

	public AjaxResponseBody() {
		msg = "";
		episodeNum = 0;
	}

	public Integer getEpisodeNum() {
		return episodeNum;
	}

	public void setEpisodeNum(Integer episodeNum) {
		this.episodeNum = episodeNum;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
