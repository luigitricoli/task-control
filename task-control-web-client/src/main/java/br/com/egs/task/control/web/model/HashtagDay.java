package br.com.egs.task.control.web.model;

import java.util.Set;

public class HashtagDay {

	private Integer day;
	private Set<Hashtag> hashtags;

	public HashtagDay(Integer day, Set<Hashtag> hashtags) {
		this.day = day;
		this.hashtags = hashtags;
	}
	
	public Integer getDay() {
		return day;
	}

	public Set<Hashtag> getHashtags() {
		return hashtags;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((day == null) ? 0 : day.hashCode());
		result = prime * result + ((hashtags == null) ? 0 : hashtags.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof HashtagDay)) {
			return false;
		}
		HashtagDay other = (HashtagDay) obj;
		if (!day.equals(other.day)) {
			return false;
		}
		if (!hashtags.equals(other.hashtags)) {
			return false;
		}
		return true;
	}

}
