package org.reallylastone.lichessbot.event.game.model;

public class White {
	public String id;
	public String name;
	public boolean provisional;
	public int rating;
	public String title;

	@Override
	public String toString() {
		return "White{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", provisional=" + provisional +
				", rating=" + rating +
				", title='" + title + '\'' +
				'}';
	}
}
