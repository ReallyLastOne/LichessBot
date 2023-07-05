package org.reallylastone.lichessbot.event.bot;

import org.reallylastone.lichessbot.event.bot.model.OnlineBotEvent;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OnlineBotEventFactory {
	private static final Gson GSON = new Gson();

	// factory method
	public static OnlineBotEvent produce(String element) {
		JsonObject event = JsonParser.parseString(element).getAsJsonObject();

		return GSON.fromJson(event, OnlineBotEvent.class);
	}
}
