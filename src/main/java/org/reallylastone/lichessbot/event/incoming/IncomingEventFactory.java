package org.reallylastone.lichessbot.event.incoming;

import org.reallylastone.lichessbot.event.incoming.model.Challenge;
import org.reallylastone.lichessbot.event.incoming.model.ChallengeCanceled;
import org.reallylastone.lichessbot.event.incoming.model.ChallengeDeclined;
import org.reallylastone.lichessbot.event.incoming.model.GameFinish;
import org.reallylastone.lichessbot.event.incoming.model.GameStart;
import org.reallylastone.lichessbot.event.incoming.model.IncomingEvent;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class IncomingEventFactory {

	public static final String CHALLENGE = "challenge";
	public static final String CHALLENGE_CANCELED = "challengeCanceled";
	public static final String CHALLENGE_DECLINED = "challengeDeclined";
	public static final String GAME_FINISH = "gameFinish";
	public static final String GAME_START = "gameStart";
	public static final String GAME = "game";
	private static final Gson GSON = new Gson();

	// factory method
	public static IncomingEvent produce(String element) {
		JsonObject event = JsonParser.parseString(element).getAsJsonObject();

		return switch (event.get("type").getAsString()) {
		case CHALLENGE -> GSON.fromJson(event.get(CHALLENGE), Challenge.class);
		case CHALLENGE_CANCELED -> GSON.fromJson(event.get(CHALLENGE), ChallengeCanceled.class);
		case CHALLENGE_DECLINED -> GSON.fromJson(event.get(CHALLENGE), ChallengeDeclined.class);
		case GAME_FINISH -> GSON.fromJson(event.get(GAME), GameFinish.class);
		case GAME_START -> GSON.fromJson(event.get(GAME), GameStart.class);
		default -> throw new IllegalArgumentException("unknown incoming event type");
		};
	}
}
