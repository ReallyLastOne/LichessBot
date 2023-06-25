package org.reallylastone.lichessbot.event.incoming.model;

public class Opponent {
	public String id;
	public String username;
	public int rating;
	public int ratingDiff;

	@Override
	public String toString() {
		return "Opponent{" +
				"id='" + id + '\'' +
				", username='" + username + '\'' +
				", rating=" + rating +
				", ratingDiff=" + ratingDiff +
				'}';
	}
}
