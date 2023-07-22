package org.reallylastone.lichessbot.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Flow;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.reallylastone.lichessbot.event.bot.model.OnlineBotEvent;

public class OnlineBotManager implements Flow.Subscriber<OnlineBotEvent> {
	private final Logger logger = Logger.getLogger(OnlineBotManager.class.getName());
	private Flow.Subscription subscription;
	private List<OnlineBotEvent> events = new ArrayList<>();
	private boolean completed = false;

	@Override
	public void onSubscribe(Flow.Subscription subscription) {
		(this.subscription = subscription).request(1);
	}

	@Override
	public void onNext(OnlineBotEvent item) {
		logger.log(Level.FINER, () -> "Received: " + item);

		if (completed) {
			events.clear();
		}
		completed = false;
		subscription.request(1);
		events.add(item);
	}

	@Override
	public void onError(Throwable ex) {
		logger.log(Level.SEVERE, () -> "Exception in OnlineBotManager: " + Arrays.toString(ex.getStackTrace()));
	}

	@Override
	public void onComplete() {
		logger.log(Level.INFO, () -> "OnlineBotManager complete");
		completed = true;
	}

	public Optional<OnlineBotEvent> getRandom() {
		if (!events.isEmpty()) {
			return Optional.ofNullable(events.get(new Random().nextInt(events.size())));
		}

		return Optional.empty();

	}
}
