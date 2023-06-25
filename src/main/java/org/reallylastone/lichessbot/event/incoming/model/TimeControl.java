package org.reallylastone.lichessbot.event.incoming.model;

public class TimeControl {
	public String type;
	public int limit;
	public int increment;
	public String show;

	@Override
	public String toString() {
		return "TimeControl{" +
				"type='" + type + '\'' +
				", limit=" + limit +
				", increment=" + increment +
				", show='" + show + '\'' +
				'}';
	}
}
