package org.reallylastone.lichessbot.utility;

public class Constants {
	private Constants() {
	}

	public static class URL {
		public static final String INCOMING_EVENTS_URL = "https://lichess.org/api/stream/event";
		public static final String BOT_GAME_EVENTS_URL = "https://lichess.org/api/bot/game/stream/{gameId}";
		public static final String ACCEPT_CHALLENGE_URL = "https://lichess.org/api/challenge/{challengeId}/accept";
		public static final String MAKE_MOVE_URL = "https://lichess.org/api/bot/game/{gameId}/move/{move}";
		public static final String ONLINE_BOTS = "https://lichess.org/api/bot/online";
		public static final String CREATE_CHALLENGE = "https://lichess.org/api/challenge/{username}";
		public static final String CANCEL_CHALLENGE = "https://lichess.org/api/challenge/{challengeId}/cancel";
		public static final String DECLINE_CHALLENGE = "https://lichess.org/api/challenge/{challengeId}/decline";
		public static final String MY_PROFILE = "https://lichess.org/api/account";

		private URL() {
		}
	}
}
