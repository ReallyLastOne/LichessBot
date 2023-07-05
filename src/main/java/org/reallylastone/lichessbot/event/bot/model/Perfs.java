package org.reallylastone.lichessbot.event.bot.model;

public class Perfs {
	public Chess960 chess960;
	public Atomic atomic;
	public RacingKings racingKings;
	public UltraBullet ultraBullet;
	public Blitz blitz;
	public KingOfTheHill kingOfTheHill;
	public Bullet bullet;
	public Correspondence correspondence;
	public Horde horde;
	public Puzzle puzzle;
	public Classical classical;
	public Rapid rapid;
	public Storm storm;
	public Racer racer;
	public Streak streak;

	@Override
	public String toString() {
		return "Perfs{" +
				"chess960=" + chess960 +
				", atomic=" + atomic +
				", racingKings=" + racingKings +
				", ultraBullet=" + ultraBullet +
				", blitz=" + blitz +
				", kingOfTheHill=" + kingOfTheHill +
				", bullet=" + bullet +
				", correspondence=" + correspondence +
				", horde=" + horde +
				", puzzle=" + puzzle +
				", classical=" + classical +
				", rapid=" + rapid +
				", storm=" + storm +
				", racer=" + racer +
				", streak=" + streak +
				'}';
	}
}