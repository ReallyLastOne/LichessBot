package org.reallylastone.lichessbot.utility;

public class Constants {
	private Constants() {
	}

	public static class URL {
		public static final String INCOMING_EVENTS_URL = "https://lichess.org/api/stream/event";
		public static final String BOT_GAME_EVENTS_URL = "https://lichess.org/api/bot/game/stream/{gameId}";
		public static final String ACCEPT_CHALLENGE_URL = "https://lichess.org/api/challenge/{challengeId}/accept";
		public static final String MAKE_MOVE_URL = "https://lichess.org/api/bot/game/{gameId}/move/{move}";

		private URL() {
		}
	}
}
