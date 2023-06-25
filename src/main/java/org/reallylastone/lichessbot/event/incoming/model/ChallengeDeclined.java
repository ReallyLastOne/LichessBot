package org.reallylastone.lichessbot.event.incoming.model;

public class ChallengeDeclined extends IncomingEvent {
	public String id;
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
	public String declineReason;
	public String declineReasonKey;

	@Override
	public String toString() {
		return "ChallengeDeclined{" +
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
				", declineReason='" + declineReason + '\'' +
				", declineReasonKey='" + declineReasonKey + '\'' +
				'}';
	}
}
