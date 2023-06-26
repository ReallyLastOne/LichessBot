package org.reallylastone.lichessbot.event.game.model;

public class GameState extends GameEvent {
	public String moves;
	public int wtime;
	public int btime;
	public int winc;
	public int binc;
	public String status;

	@Override
	public String toString() {
		return "GameState{" +
				"moves='" + moves + '\'' +
				", wtime=" + wtime +
				", btime=" + btime +
				", winc=" + winc +
				", binc=" + binc +
				", status='" + status + '\'' +
				'}';
	}
}
