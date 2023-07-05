package org.reallylastone.lichessbot.core;

import java.io.IOException;
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
				for (Challenge activeChallenge : activeChallenges) {
					if (!activeChallenge.destUser.name.equals("GetFun") && activeChallenge.timeControl.limit <= 300 && activeChallenge.timeControl.increment <= 2) {
						HttpRequestSender.acceptChallenge(activeChallenge.id);
					} else if (activeChallenge.destUser.name.equals("GetFun")) {
						HttpRequestSender.cancelChallenge(activeChallenge.id);
					}
				}
			} catch (IOException | InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
}