package org.reallylastone.lichessbot.event.bot.model;

public class OnlineBotEvent {
	public String id;
	public String username;
	public Perfs perfs;
	public long createdAt;
	public boolean disabled;
	public boolean tosViolation;
	public Profile profile;
	public long seenAt;
	public boolean patron;
	public boolean verified;
	public PlayTime playTime;
	public String title;

	@Override
	public String toString() {
		return "OnlineBotEvent{" +
				"id='" + id + '\'' +
				", username='" + username + '\'' +
				", perfs=" + perfs +
				", createdAt=" + createdAt +
				", disabled=" + disabled +
				", tosViolation=" + tosViolation +
				", profile=" + profile +
				", seenAt=" + seenAt +
				", patron=" + patron +
				", verified=" + verified +
				", playTime=" + playTime +
				", title='" + title + '\'' +
				'}';
	}
}
