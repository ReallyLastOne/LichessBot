package org.reallylastone.lichessbot.core;

import chariot.model.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reallylastone.lichessbot.utility.Context;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ApplicationManager {
	private final Logger logger = LogManager.getLogger(ApplicationManager.class.getName());
	private final ChallengeHandlerStrategy strategy;
	private final Map<ZonedDateTime, Event.ChallengeCreatedEvent> activeChallenges = new Hashtable<>();
	private final List<Event.GameStartEvent> activeGames = new ArrayList<>();

	public ApplicationManager(ChallengeHandlerStrategy strategy) {
		this.strategy = strategy;
		Thread.startVirtualThread(() -> {
			logger.info("Subscribing to application events");
			Context.auth().bot().connect().stream().forEach(this::onNext);
		});
		scheduleChallengeHandling();
	}

	public void onNext(Event item) {
		logger.debug("Received event {}", item);

		// synchronizing internal state
		switch (item) {
		case Event.ChallengeCreatedEvent challengeCreated ->
			activeChallenges.put(ZonedDateTime.now(), challengeCreated);
		case Event.ChallengeDeclinedEvent challengeDeclined -> removeChallengeById(challengeDeclined.id());
		case Event.ChallengeCanceledEvent challengeCanceled -> removeChallengeById(challengeCanceled.id());
		case Event.GameStartEvent gameStart -> onStart(gameStart);
		case Event.GameStopEvent gameFinish ->
			activeGames.removeIf(activeGame -> Objects.equals(activeGame.id(), gameFinish.gameId()));
		default -> logger.error("Unknown event received {}", item);
		}
	}

	@SuppressWarnings("resource")
	private void scheduleChallengeHandling() {
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleWithFixedDelay(() -> strategy.handle(activeChallenges, activeGames), 1, 1, TimeUnit.SECONDS);
	}

	private void onStart(Event.GameStartEvent event) {
		removeChallengeById(event.id());
		activeGames.add(event);
		new GameManager(event);
	}

	private void removeChallengeById(String id) {
		activeChallenges.entrySet().removeIf(entry -> Objects.equals(entry.getValue().id(), id));
	}
}
