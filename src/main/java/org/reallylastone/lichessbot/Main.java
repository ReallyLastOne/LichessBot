package org.reallylastone.lichessbot;

import org.reallylastone.lichessbot.core.ChallengeManager;
import org.reallylastone.lichessbot.core.OnlineBotManager;
import org.reallylastone.lichessbot.event.GenericEventProcessor;
import org.reallylastone.lichessbot.event.bot.OnlineBotEventFactory;
import org.reallylastone.lichessbot.event.bot.model.OnlineBotEvent;
import org.reallylastone.lichessbot.event.incoming.IncomingEventFactory;
import org.reallylastone.lichessbot.event.incoming.model.IncomingEvent;
import org.reallylastone.lichessbot.utility.Context;

import static org.reallylastone.lichessbot.utility.Constants.URL.*;

public class Main {
	public static void main(String[] args) {
		GenericEventProcessor<IncomingEvent> listener = new GenericEventProcessor<>(Context.getClient(),
				IncomingEventFactory::produce);

		listener.start(INCOMING_EVENTS_URL);

		GenericEventProcessor<OnlineBotEvent> onlineBotEventProcessor = new GenericEventProcessor<>(
				Context.getClient(), OnlineBotEventFactory::produce);
		onlineBotEventProcessor.start(ONLINE_BOTS);

		OnlineBotManager onlineBotManager = new OnlineBotManager();
		onlineBotEventProcessor.subscribe(onlineBotManager);

		ChallengeManager manager = new ChallengeManager(Context.getMaxActiveChallengeHandlerStrategy(), onlineBotManager);
		listener.subscribe(manager);
	}
}