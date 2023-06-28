package org.reallylastone.lichessbot.event.incoming.model;

public class Challenge extends ChallengeItem {
	public String url;
	public String status;
	public Compat compat;
	public Challenger challenger;
	public DestUser destUser;
	public Variant variant;
	public boolean rated;
	public TimeControl timeControl;
	public String color;
	public String finalColor;
	public String speed;
	public Perf perf;

	@Override
	public String toString() {
		return "Challenge{" +
				"id='" + id + '\'' +
				", url='" + url + '\'' +
				", status='" + status + '\'' +
				", compat=" + compat +
				", challenger=" + challenger +
				", destUser=" + destUser +
				", variant=" + variant +
				", rated=" + rated +
				", timeControl=" + timeControl +
				", color='" + color + '\'' +
				", finalColor='" + finalColor + '\'' +
				", speed='" + speed + '\'' +
				", perf=" + perf +
				'}';
	}
}