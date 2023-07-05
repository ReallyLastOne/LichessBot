package org.reallylastone.lichessbot.event.game.model;

public class OpponentGone extends GameEvent {
	public boolean gone;
	public Integer claimWinInSeconds;

	@Override
	public String toString() {
		return "OpponentGone{" +
				"type='" + type + '\'' +
				", gone=" + gone +
				", claimWinInSeconds=" + claimWinInSeconds +
				'}';
	}
}
