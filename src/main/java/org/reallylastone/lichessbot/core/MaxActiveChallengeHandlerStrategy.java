package org.reallylastone.lichessbot.core;

import java.util.List;

import org.reallylastone.lichessbot.event.incoming.model.Challenge;
import org.reallylastone.lichessbot.event.incoming.model.GameStart;
import org.reallylastone.lichessbot.http.HttpRequestSender;

public class MaxActiveChallengeHandlerStrategy implements ChallengeHandlerStrategy {
	private static final int DEFAULT_MAX_ACTIVE_GAMES = 3;
	private final int maxActiveGames;

	public MaxActiveChallengeHandlerStrategy(int maxActiveGames) {
		if (maxActiveGames > 0) {
			this.maxActiveGames = maxActiveGames;
		} else {
			throw new IllegalArgumentException("illegal number of maximum active games");
		}
	}

	public MaxActiveChallengeHandlerStrategy() {
		this.maxActiveGames = DEFAULT_MAX_ACTIVE_GAMES;
	}

	@Override
	public void handle(List<Challenge> activeChallenges, List<GameStart> activeGames) {
		if (activeGames.size() < maxActiveGames && !activeChallenges.isEmpty()) {
			try {
				HttpRequestSender.acceptChallenge(activeChallenges.get(0).id);
			} catch (Exception e) {
			}
		}
	}
}
