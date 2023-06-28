package org.reallylastone.lichessbot.core;

import org.reallylastone.lichessbot.event.incoming.model.Challenge;
import org.reallylastone.lichessbot.event.incoming.model.GameStart;

import java.util.List;

public interface ChallengeHandlerStrategy {
    void handle(List<Challenge> activeChallenges, List<GameStart> activeGames);
}
