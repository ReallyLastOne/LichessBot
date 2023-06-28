package org.reallylastone.lichessbot.core;

import java.util.concurrent.Flow;

import org.reallylastone.lichessbot.event.game.model.GameEvent;
import org.reallylastone.lichessbot.event.game.model.GameFull;
import org.reallylastone.lichessbot.event.game.model.GameState;

public class GameManager implements Flow.Subscriber<GameEvent> {
	private Flow.Subscription subscription;
	private GameState currentState;

	@Override
	public void onSubscribe(Flow.Subscription subscription) {
		(this.subscription = subscription).request(1);
	}

	@Override
	public void onNext(GameEvent item) {
		subscription.request(1);

		if (item instanceof GameFull full) {
			currentState = full.state;
		} else if (item instanceof GameState state) {
			currentState = state;
		}
	}

	@Override
	public void onError(Throwable ex) {
	}

	@Override
	public void onComplete() {
	}

}
