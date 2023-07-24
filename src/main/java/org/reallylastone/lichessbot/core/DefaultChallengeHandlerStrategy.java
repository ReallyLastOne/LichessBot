package org.reallylastone.lichessbot.core;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.reallylastone.lichessbot.event.incoming.model.Challenge;
import org.reallylastone.lichessbot.event.incoming.model.GameStart;
import org.reallylastone.lichessbot.http.HttpRequestSender;

public class DefaultChallengeHandlerStrategy implements ChallengeHandlerStrategy {
	private final int maxActiveGames;
	private final Logger logger = Logger.getLogger(DefaultChallengeHandlerStrategy.class.getName());
	private final Supplier<String> opponentSupplier;

	public DefaultChallengeHandlerStrategy(int maxActiveGames, Supplier<String> opponentSupplier) {
		if (maxActiveGames > 0) {
			this.maxActiveGames = maxActiveGames;
		} else {
			throw new IllegalArgumentException("illegal number of maximum active games");
		}
		this.opponentSupplier = opponentSupplier;
	}

	@Override
	public void handle(Map<LocalDateTime, Challenge> activeChallenges, Collection<GameStart> activeGames) {
		// delete non-active challenges because lichess automatically cancel challenge
		// after 20 seconds (if send with
		// keepAliveStream=false) but does not send any event
		activeChallenges.entrySet()
				.removeIf(challenge -> challenge.getKey().isBefore(LocalDateTime.now().minusSeconds(20)));

		// we accept only challenges that matches given predicate, so we decline any
		// other
		Predicate<Challenge> entryPredicate = e -> e.rated && e.timeControl.limit <= 300
				&& e.timeControl.increment <= 2;
		activeChallenges.values().stream().filter(entryPredicate.negate())
				.forEach(challenge -> HttpRequestSender.declineChallenge(challenge.id));

		if (activeGames.size() > maxActiveGames)
			return;

		// accept first matching challenge or create new one
		activeChallenges.values().stream().filter(entryPredicate).findFirst()
				.ifPresentOrElse(challenge -> HttpRequestSender.acceptChallenge(challenge.id), createNewChallenge());
	}

	private Runnable createNewChallenge() {
		return () -> {
			String random = opponentSupplier.get();
			logger.log(Level.INFO, () -> "Sending game challenge to %s".formatted(random));

			Map<String, String> challengeParams = Map.of("rated", "false", "clock.limit", "180", "clock.increment", "0",
					"color", "random", "variant", "standard", "keepAliveStream", "false");
			HttpRequestSender.createChallenge(challengeParams.entrySet().stream()
					.map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
					.collect(Collectors.joining("&")), random);
		};
	}
}
