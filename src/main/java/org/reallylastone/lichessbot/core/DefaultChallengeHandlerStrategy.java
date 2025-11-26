package org.reallylastone.lichessbot.core;

import chariot.model.Event;
import chariot.model.GameType;
import chariot.model.RealTime;
import chariot.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reallylastone.lichessbot.utility.Context;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.reallylastone.lichessbot.utility.Utils.isOwnChallengePresent;

public class DefaultChallengeHandlerStrategy implements ChallengeHandlerStrategy {
	private final int maxActiveGames;
	private final Logger logger = LogManager.getLogger(DefaultChallengeHandlerStrategy.class.getName());

	public DefaultChallengeHandlerStrategy(int maxActiveGames) {
		if (maxActiveGames > 0) {
			this.maxActiveGames = maxActiveGames;
		} else {
			throw new IllegalArgumentException("illegal number of maximum active games");
		}
	}

	private static boolean canAccept(Event.ChallengeCreatedEvent e) {
		// accept only rated challenge with initial clock <= 3 minutes
		// and increment <= 2 seconds
		GameType gameType = e.challenge().gameType();
		if (!gameType.rated())
			return false;
		if (!(gameType.timeControl() instanceof RealTime rt)) {
			return false;
		}
		if (rt.increment().toMillis() > Duration.ofSeconds(2).toMillis()) {
			return false;
		}
		return rt.initial().toMillis() <= Duration.ofMinutes(3).toMillis();
	}

	private static Optional<User> getRandomBot() {
		List<User> botsOnline = Context.auth().bot().botsOnline().stream().toList();

		return botsOnline.stream().skip(ThreadLocalRandom.current().nextInt(botsOnline.stream().toList().size()))
				.findFirst();
	}

	@Override
	public void handle(Map<ZonedDateTime, Event.ChallengeCreatedEvent> activeChallenges,
			Collection<Event.GameStartEvent> activeGames) {
		logger.trace("Handling active challenges");
		// delete non-active challenges because lichess automatically cancel challenge
		// after 20 seconds (if send with
		// keepAliveStream=false) but does not send any event
		// we don't explicitly decline challenge from API
		// as it could lead to starvation - no game progress would be made
		activeChallenges.entrySet().removeIf(challenge -> Duration.between(challenge.getKey(), ZonedDateTime.now())
				.toMillis() > Duration.ofSeconds(20).toMillis());

		// if any challenge is ours: we won't be accepting new ones to prevent duplicate
		// games started
		if (isOwnChallengePresent(activeChallenges)) {
			return;
		}
		if (activeGames.size() >= maxActiveGames) {
			logger.trace("Active games reached its limit! Not accepting nor creating new game");
			return;
		}

		// TODO: generify the challenges that can be accepted - load from configuration
		// accept first matching challenge or create new one
		Optional<Event.ChallengeCreatedEvent> challenge = activeChallenges.values().stream()
				.filter(DefaultChallengeHandlerStrategy::canAccept).findFirst();
		if (challenge.isPresent()) {
			logger.info("Accepting {} challenge", challenge.get().id());
			Context.auth().bot().acceptChallenge(challenge.get().id());
			return;
		}

        // TODO: make the challenge customizable
		Optional<User> random = getRandomBot();
		if (random.isPresent()) {
			logger.info("Sending game challenge to {}", random);
			Context.auth().bot().challenge(random.get().id(),
					challengeBuilder -> challengeBuilder.clockBlitz3m0s().rated());
		}
	}
}
