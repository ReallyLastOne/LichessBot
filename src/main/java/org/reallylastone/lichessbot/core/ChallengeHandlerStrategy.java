package org.reallylastone.lichessbot.core;

import chariot.model.Event;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Map;

public interface ChallengeHandlerStrategy {
	void handle(Map<ZonedDateTime, Event.ChallengeCreatedEvent> activeChallenges,
			Collection<Event.GameStartEvent> activeGames);
}
