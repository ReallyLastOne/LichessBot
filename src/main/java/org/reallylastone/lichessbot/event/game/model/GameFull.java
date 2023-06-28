package org.reallylastone.lichessbot.event.game.model;

public class GameFull extends GameEvent {
	public String id;
	public boolean rated;
	public Variant variant;
	public Clock clock;
	public String speed;
	public Perf perf;
	public long createdAt;
	public White white;
	public Black black;
	public String initialFen;
	public GameState state;

	@Override
	public String toString() {
		return "GameFull{" +
				"id='" + id + '\'' +
				", rated=" + rated +
				", variant=" + variant +
				", clock=" + clock +
				", speed='" + speed + '\'' +
				", perf=" + perf +
				", createdAt=" + createdAt +
				", white=" + white +
				", black=" + black +
				", initialFen='" + initialFen + '\'' +
				", state=" + state +
				'}';
	}
}
