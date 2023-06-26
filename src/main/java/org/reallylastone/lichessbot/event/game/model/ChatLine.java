package org.reallylastone.lichessbot.event.game.model;

public class ChatLine extends GameEvent {
	public String username;
	public String text;
	public String room;

	@Override
	public String toString() {
		return "ChatLine{" +
				"username='" + username + '\'' +
				", text='" + text + '\'' +
				", room='" + room + '\'' +
				'}';
	}
}