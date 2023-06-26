package org.reallylastone.lichessbot.event.game.model;

class Clock {
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