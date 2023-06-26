package org.reallylastone.lichessbot.event.game.model;

class State {
	public String type;
	public String moves;
	public int wtime;
	public int btime;
	public int winc;
	public int binc;
	public String status;

	@Override
	public String toString() {
		return "State{" +
				"type='" + type + '\'' +
				", moves='" + moves + '\'' +
				", wtime=" + wtime +
				", btime=" + btime +
				", winc=" + winc +
				", binc=" + binc +
				", status='" + status + '\'' +
				'}';
	}
}
