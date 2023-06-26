package org.reallylastone.lichessbot.event.game.model;

public class OpponentGone extends GameEvent {
	public String type;
	public boolean gone;
	public int claimWinInSeconds;

	@Override
	public String toString() {
		return "OpponentGone{" +
				"type='" + type + '\'' +
				", gone=" + gone +
				", claimWinInSeconds=" + claimWinInSeconds +
				'}';
	}
}
