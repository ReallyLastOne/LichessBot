package org.reallylastone.lichessbot.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Flow;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reallylastone.lichessbot.event.bot.model.OnlineBotEvent;

public class OnlineBotManager implements Flow.Subscriber<OnlineBotEvent> {
	private final Logger logger = LogManager.getLogger(OnlineBotManager.class.getName());
	private final List<OnlineBotEvent> events = new ArrayList<>();
	private final Random random = new Random();
	private Flow.Subscription subscription;

	@Override
	public void onSubscribe(Flow.Subscription subscription) {
		this.subscription = subscription;
		subscription.request(1);
	}

	@Override
	public void onNext(OnlineBotEvent item) {
		logger.log(Level.DEBUG, () -> "Received: " + item);
		subscription.request(1);
		events.add(item);
	}

	@Override
	public void onError(Throwable ex) {
		logger.log(Level.ERROR, () -> "Exception in OnlineBotManager %s".formatted(ex.getMessage()), ex);
	}

	@Override
	public void onComplete() {
		logger.log(Level.INFO, () -> "OnlineBotManager complete");
	}

	public OnlineBotEvent getRandom() {
		if (events.isEmpty()) {
			var result = new OnlineBotEvent();
			result.username = "maia9";
			return result;
		}

		return events.get(random.nextInt(events.size()));
	}
}
