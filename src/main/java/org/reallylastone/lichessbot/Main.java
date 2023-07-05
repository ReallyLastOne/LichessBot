package org.reallylastone.lichessbot;

import static org.reallylastone.lichessbot.utility.Constants.URL.INCOMING_EVENTS_URL;

import org.reallylastone.lichessbot.core.ChallengeManager;
import org.reallylastone.lichessbot.event.GenericEventProcessor;
import org.reallylastone.lichessbot.event.incoming.IncomingEventFactory;
import org.reallylastone.lichessbot.event.incoming.model.IncomingEvent;
import org.reallylastone.lichessbot.utility.Context;

public class Main {
	public static void main(String[] args) {
		GenericEventProcessor<IncomingEvent> listener = new GenericEventProcessor<>(Context.getClient(),
				IncomingEventFactory::produce);

		listener.start(INCOMING_EVENTS_URL);

		ChallengeManager manager = new ChallengeManager(Context.getMaxActiveChallengeHandlerStrategy());
		listener.subscribe(manager);
	}
}