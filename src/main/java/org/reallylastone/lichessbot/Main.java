package org.reallylastone.lichessbot;

import static org.reallylastone.lichessbot.utility.Constants.URL.INCOMING_EVENTS_URL;
import static org.reallylastone.lichessbot.utility.Constants.URL.ONLINE_BOTS;

import org.reallylastone.lichessbot.core.ChallengeManager;
import org.reallylastone.lichessbot.core.OnlineBotManager;
import org.reallylastone.lichessbot.event.GenericEventProcessor;
import org.reallylastone.lichessbot.event.bot.OnlineBotEventFactory;
import org.reallylastone.lichessbot.event.bot.model.OnlineBotEvent;
import org.reallylastone.lichessbot.event.incoming.IncomingEventFactory;
import org.reallylastone.lichessbot.event.incoming.model.IncomingEvent;
import org.reallylastone.lichessbot.utility.Context;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		GenericEventProcessor<IncomingEvent> incomingEventsProcessor = new GenericEventProcessor<>(Context.getClient(),
				IncomingEventFactory::produce);
		incomingEventsProcessor.start(INCOMING_EVENTS_URL);

		OnlineBotManager onlineBotManager = new OnlineBotManager();

		GenericEventProcessor<OnlineBotEvent> onlineBotEventProcessor = new GenericEventProcessor<>(Context.getClient(),
				OnlineBotEventFactory::produce);
		onlineBotEventProcessor.start(ONLINE_BOTS);
		onlineBotEventProcessor.subscribe(onlineBotManager);

		ChallengeManager manager = new ChallengeManager(
				Context.getMaxActiveChallengeHandlerStrategy(() -> onlineBotManager.getRandom().username));
		incomingEventsProcessor.subscribe(manager);
		Thread.currentThread().join();
	}
}