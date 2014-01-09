package br.com.egs.task.control.web.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Task {

	private Integer startDay;
	private Integer intervalDay;
	private Stage stage;
	private String description;
	private Set<HashtagDay> hashtagDays;

	public Task() {
		this.startDay = 6;
		this.intervalDay = 2;
		this.stage = Stage.finished;
		this.description = "SR555 - Update.";
		this.hashtagDays = new HashSet<HashtagDay>();

		Set<Hashtag> tags = new HashSet<Hashtag>(Arrays.asList(new Hashtag[] { Hashtag.late, Hashtag.overtime }));
		this.hashtagDays.add(new HashtagDay(1, tags));
	}

	public Integer getStartDay() {
		return startDay;
	}

	public Integer getIntervalDay() {
		return intervalDay;
	}

	public Stage getStage() {
		return stage;
	}

	public String getDescription() {
		return description;
	}

	public Set<HashtagDay> getHashtagDays() {
		return hashtagDays;
	}

}
