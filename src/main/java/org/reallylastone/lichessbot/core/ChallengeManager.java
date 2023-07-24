package org.reallylastone.lichessbot.core;

import static org.reallylastone.lichessbot.utility.Constants.URL.BOT_GAME_EVENTS_URL;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.reallylastone.lichessbot.event.GenericEventProcessor;
import org.reallylastone.lichessbot.event.game.GameEventFactory;
import org.reallylastone.lichessbot.event.game.model.GameEvent;
import org.reallylastone.lichessbot.event.incoming.model.Challenge;
import org.reallylastone.lichessbot.event.incoming.model.ChallengeCanceled;
import org.reallylastone.lichessbot.event.incoming.model.ChallengeDeclined;
import org.reallylastone.lichessbot.event.incoming.model.GameFinish;
import org.reallylastone.lichessbot.event.incoming.model.GameStart;
import org.reallylastone.lichessbot.event.incoming.model.IncomingEvent;
import org.reallylastone.lichessbot.utility.Context;

public class ChallengeManager implements Flow.Subscriber<IncomingEvent> {
	private final Logger logger = Logger.getLogger(ChallengeManager.class.getName());
	private final ChallengeHandlerStrategy strategy;
	private final Map<LocalDateTime, Challenge> activeChallenges = new Hashtable<>();
	private final List<GameStart> activeGames = new ArrayList<>();
	private Flow.Subscription subscription;

	public ChallengeManager(ChallengeHandlerStrategy strategy) {
		this.strategy = strategy;
		scheduleChallengeHandling();
	}

	@Override
	public void onSubscribe(Flow.Subscription subscription) {
		this.subscription = subscription;
		subscription.request(1);
	}

	@Override
	public void onNext(IncomingEvent item) {
		subscription.request(1);
		logger.log(Level.FINER, () -> "Received: " + item);

		// synchronizing internal state
		switch (item) {
		case Challenge challenge -> activeChallenges.put(LocalDateTime.now(), challenge);
		case ChallengeDeclined challengeDeclined -> removeChallengeById(challengeDeclined.id);
		case ChallengeCanceled challengeCanceled -> removeChallengeById(challengeCanceled.id);
		case GameStart gameStart -> onStart(gameStart);
		case GameFinish gameFinish ->
			activeGames.removeIf(activeGame -> Objects.equals(activeGame.id, gameFinish.gameId));
		default -> logger.log(Level.SEVERE, "unknown incoming event type");
		}
	}

	@Override
	public void onError(Throwable ex) {
		logger.log(Level.SEVERE, () -> "Exception in ChallengeManager %s".formatted(ex.getMessage()));
	}

	@Override
	public void onComplete() {
		logger.log(Level.INFO, () -> "ChallengeManager complete");
	}

	private void scheduleChallengeHandling() {
		// with try-resource it doesn't work
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(() -> strategy.handle(activeChallenges, activeGames), 0, 10, TimeUnit.SECONDS);
	}

	private void onStart(GameStart gameStart) {
		removeChallengeById(gameStart.id);
		activeGames.add(gameStart);
		GenericEventProcessor<GameEvent> gameListener = new GenericEventProcessor<>(Context.getClient(),
				GameEventFactory::produce);
		gameListener.start(BOT_GAME_EVENTS_URL.replace("{gameId}", gameStart.gameId));
		GameManager gameManager = new GameManager();
		gameListener.subscribe(gameManager);
	}

	private void removeChallengeById(String id) {
		activeChallenges.entrySet().removeIf(entry -> Objects.equals(entry.getValue().id, id));
	}
}
