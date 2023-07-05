package org.reallylastone.lichessbot.event.game;

import org.reallylastone.lichessbot.event.game.model.ChatLine;
import org.reallylastone.lichessbot.event.game.model.GameEvent;
import org.reallylastone.lichessbot.event.game.model.GameFull;
import org.reallylastone.lichessbot.event.game.model.GameState;
import org.reallylastone.lichessbot.event.game.model.OpponentGone;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GameEventFactory {
	private static final Gson GSON = new Gson();

	// factory method
	public static GameEvent produce(String element) {
		JsonObject event = JsonParser.parseString(element).getAsJsonObject();

		return switch (event.get("type").getAsString()) {
		case "gameFull" -> GSON.fromJson(event, GameFull.class);
		case "gameState" -> GSON.fromJson(event, GameState.class);
		case "chatLine" -> GSON.fromJson(event, ChatLine.class);
		case "opponentGone" -> GSON.fromJson(event, OpponentGone.class);
		default -> throw new IllegalArgumentException("unknown game event type: " + event.get("type").getAsString());
		};
	}
}
