package org.reallylastone.lichessbot.event.game.model;

public class Clock {
	public int initial;
	public int increment;

	@Override
	public String toString() {
		return "Clock{" +
				"initial=" + initial +
				", increment=" + increment +
				'}';
	}
}