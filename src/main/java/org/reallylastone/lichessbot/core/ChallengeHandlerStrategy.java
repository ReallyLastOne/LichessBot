package org.reallylastone.lichessbot.core;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

import org.reallylastone.lichessbot.event.incoming.model.Challenge;
import org.reallylastone.lichessbot.event.incoming.model.GameStart;

public interface ChallengeHandlerStrategy {
	void handle(Map<LocalDateTime, Challenge> activeChallenges, Collection<GameStart> activeGames);
}
