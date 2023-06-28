package org.reallylastone.lichessbot.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;

import org.reallylastone.lichessbot.event.game.GameEventFactory;
import org.reallylastone.lichessbot.event.game.GameEventsProcessor;
import org.reallylastone.lichessbot.event.game.model.GameEvent;
import org.reallylastone.lichessbot.event.incoming.model.Challenge;
import org.reallylastone.lichessbot.event.incoming.model.ChallengeCanceled;
import org.reallylastone.lichessbot.event.incoming.model.ChallengeDeclined;
import org.reallylastone.lichessbot.event.incoming.model.ChallengeItem;
import org.reallylastone.lichessbot.event.incoming.model.GameFinish;
import org.reallylastone.lichessbot.event.incoming.model.GameStart;
import org.reallylastone.lichessbot.event.incoming.model.IncomingEvent;
import org.reallylastone.lichessbot.utility.Context;

public class ChallengeManager implements Flow.Subscriber<IncomingEvent> {
	private final ChallengeHandlerStrategy strategy;
	private List<Challenge> activeChallenges = new ArrayList<>();
	private List<GameStart> activeGames = new ArrayList<>();
	private Flow.Subscription subscription;

	public ChallengeManager(ChallengeHandlerStrategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public void onSubscribe(Flow.Subscription subscription) {
		(this.subscription = subscription).request(1);
	}

	@Override
	public void onNext(IncomingEvent item) {
		subscription.request(1);

		if (item instanceof Challenge challenge) {
			activeChallenges.add(challenge);
		} else if (item instanceof ChallengeDeclined || item instanceof ChallengeCanceled) {
			activeChallenges = activeChallenges.stream().filter(e -> !Objects.equals(e.id, ((ChallengeItem) item).id))
					.collect(Collectors.toList());
		} else if (item instanceof GameStart start) {
			activeGames.add(start);
			GameEventsProcessor<GameEvent> gameListener = new GameEventsProcessor<>(Context.getClient(),
					GameEventFactory::produce);
			gameListener.start(start.gameId);
		} else if (item instanceof GameFinish finish) {
			activeGames = activeGames.stream().filter(e -> !Objects.equals(e.id, finish.gameId))
					.collect(Collectors.toList());
		}

		strategy.handle(activeChallenges, activeGames);
	}

	@Override
	public void onError(Throwable ex) {
	}

	@Override
	public void onComplete() {
	}

}
