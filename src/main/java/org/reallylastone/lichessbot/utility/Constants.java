package org.reallylastone.lichessbot.utility;

public class Constants {
	private Constants() {
	}

	public static class URL {
		public static final String INCOMING_EVENTS_URL = "https://lichess.org/api/stream/event";
		public static final String BOT_GAME_EVENTS_URL = "https://lichess.org/api/bot/game/stream/{gameId}";
		public static final String GAME_EVENTS_URL = "https://lichess.org/api/board/game/stream/{gameId}";

		private URL() {
		}
	}
}
