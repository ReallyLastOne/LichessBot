package org.reallylastone.lichessbot;

import static org.reallylastone.lichessbot.utility.Constants.URL.INCOMING_EVENTS_URL;
import static org.reallylastone.lichessbot.utility.Constants.URL.ONLINE_BOTS;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reallylastone.lichessbot.core.ChallengeManager;
import org.reallylastone.lichessbot.core.OnlineBotManager;
import org.reallylastone.lichessbot.event.GenericEventProcessor;
import org.reallylastone.lichessbot.event.bot.OnlineBotEventFactory;
import org.reallylastone.lichessbot.event.bot.model.OnlineBotEvent;
import org.reallylastone.lichessbot.event.incoming.IncomingEventFactory;
import org.reallylastone.lichessbot.event.incoming.model.IncomingEvent;
import org.reallylastone.lichessbot.utility.Context;

public class Main {
	private static final Logger logger = LogManager.getLogger(Main.class.getName());
	public static void main(String[] args) throws InterruptedException {
		logger.log(Level.INFO, () -> "TEST");
		try (GenericEventProcessor<IncomingEvent> incomingEventsProcessor = new GenericEventProcessor<>(
				Context.getClient(), IncomingEventFactory::produce);
				GenericEventProcessor<OnlineBotEvent> onlineBotEventProcessor = new GenericEventProcessor<>(
						Context.getClient(), OnlineBotEventFactory::produce)) {
			incomingEventsProcessor.start(INCOMING_EVENTS_URL);
			OnlineBotManager onlineBotManager = new OnlineBotManager();

			onlineBotEventProcessor.start(ONLINE_BOTS);
			onlineBotEventProcessor.subscribe(onlineBotManager);
			ChallengeManager manager = new ChallengeManager(
					Context.getMaxActiveChallengeHandlerStrategy(() -> onlineBotManager.getRandom().username));
			incomingEventsProcessor.subscribe(manager);
			Thread.currentThread().join();
		}
	}
}