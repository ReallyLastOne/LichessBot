package org.reallylastone.lichessbot.core;

import static org.reallylastone.lichessbot.utility.Constants.URL.BOT_GAME_EVENTS_URL;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.reallylastone.lichessbot.event.GenericEventProcessor;
import org.reallylastone.lichessbot.event.bot.model.OnlineBotEvent;
import org.reallylastone.lichessbot.event.game.GameEventFactory;
import org.reallylastone.lichessbot.event.game.model.GameEvent;
import org.reallylastone.lichessbot.event.incoming.model.Challenge;
import org.reallylastone.lichessbot.event.incoming.model.ChallengeCanceled;
import org.reallylastone.lichessbot.event.incoming.model.ChallengeDeclined;
import org.reallylastone.lichessbot.event.incoming.model.ChallengeItem;
import org.reallylastone.lichessbot.event.incoming.model.GameFinish;
import org.reallylastone.lichessbot.event.incoming.model.GameStart;
import org.reallylastone.lichessbot.event.incoming.model.IncomingEvent;
import org.reallylastone.lichessbot.http.HttpRequestSender;
import org.reallylastone.lichessbot.utility.Context;

public class ChallengeManager implements Flow.Subscriber<IncomingEvent> {
	private final Logger logger = Logger.getLogger(ChallengeManager.class.getName());
	private final ChallengeHandlerStrategy strategy;
	private final Thread noChallengesListenerThread;
	private List<Challenge> activeChallenges = new ArrayList<>();
	private List<GameStart> activeGames = new ArrayList<>();
	private Flow.Subscription subscription;

	public ChallengeManager(ChallengeHandlerStrategy strategy, OnlineBotManager onlineBotManager) {
		this.strategy = strategy;
		noChallengesListenerThread = new Thread(() -> {
			Challenge repeated = null;
			while (true) {
				try {
					if (activeChallenges.isEmpty() && activeGames.isEmpty()) {
						Optional<OnlineBotEvent> random = onlineBotManager.getRandom();
						if (random.isPresent()) {
							Map<String, String> challengeParams = Map.of("rated", "false", "clock.limit", "180",
									"clock.increment", "0", "color", "random", "variant", "standard", "keepAliveStream",
									"false");
							HttpRequestSender.createChallenge(challengeParams.entrySet().stream().map(
									e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
									.collect(Collectors.joining("&")), "Dardd");
						}
					} else if (activeChallenges.size() == 1 && activeGames.isEmpty()) {
						if (repeated != null) {
							if (repeated.destUser.name.equals("GetFun")) {
								HttpRequestSender.declineChallenge(activeChallenges.get(0).id);
							} else {
								HttpRequestSender.cancelChallenge(activeChallenges.get(0).id);
							}
							repeated = null;
						} else {
							repeated = activeChallenges.get(0);
						}
					}
					Thread.sleep(10_000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		noChallengesListenerThread.start();
	}

	@Override
	public void onSubscribe(Flow.Subscription subscription) {
		(this.subscription = subscription).request(1);
	}

	@Override
	public void onNext(IncomingEvent item) {
		subscription.request(1);
		logger.log(Level.FINER, () -> "Received: " + item);

		if (item instanceof Challenge challenge) {
			activeChallenges.add(challenge);
		} else if (item instanceof ChallengeDeclined || item instanceof ChallengeCanceled) {
			activeChallenges = activeChallenges.stream().filter(e -> !Objects.equals(e.id, ((ChallengeItem) item).id))
					.collect(Collectors.toList());
		} else if (item instanceof GameStart start) {
			activeChallenges = activeChallenges.stream().filter(e -> !Objects.equals(e.id, start.id))
					.collect(Collectors.toList());
			activeGames.add(start);
			GenericEventProcessor<GameEvent> gameListener = new GenericEventProcessor<>(Context.getClient(),
					GameEventFactory::produce);
			gameListener.start(BOT_GAME_EVENTS_URL.replace("{gameId}", start.gameId));
			GameManager gameManager = new GameManager();
			gameListener.subscribe(gameManager);
		} else if (item instanceof GameFinish finish) {
			activeGames = activeGames.stream().filter(e -> !Objects.equals(e.id, finish.gameId))
					.collect(Collectors.toList());
		}

		strategy.handle(activeChallenges, activeGames);
	}

	@Override
	public void onError(Throwable ex) {
		logger.log(Level.SEVERE, () -> "Exception in GameManager: " + Arrays.toString(ex.getStackTrace()));
	}

	@Override
	public void onComplete() {
		logger.log(Level.INFO, () -> "ChallengeManager complete");
	}

}
