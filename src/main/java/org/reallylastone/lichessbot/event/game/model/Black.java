package org.reallylastone.lichessbot.event.game.model;

class Black {
	public String id;
	public String name;
	public int rating;

	@Override
	public String toString() {
		return "Black{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", rating=" + rating +
				'}';
	}
}
