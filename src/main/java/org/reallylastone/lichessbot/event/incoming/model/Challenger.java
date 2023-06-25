package org.reallylastone.lichessbot.event.incoming.model;

public class Challenger {
	public String id;
	public String name;
	public String title;
	public int rating;
	public boolean patron;
	public boolean online;
	public int lag;

	@Override
	public String toString() {
		return "Challenger{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", title='" + title + '\'' +
				", rating=" + rating +
				", patron=" + patron +
				", online=" + online +
				", lag=" + lag +
				'}';
	}
}
