package org.reallylastone.lichessbot;

import org.reallylastone.lichessbot.core.Context;
import org.reallylastone.lichessbot.event.incoming.IncomingEventFactory;
import org.reallylastone.lichessbot.event.incoming.IncomingEventsProcessor;
import org.reallylastone.lichessbot.event.incoming.model.IncomingEvent;

import static org.reallylastone.lichessbot.core.Constants.URL.INCOMING_EVENTS_URL;

public class Main {
	public static void main(String[] args) {
		IncomingEventsProcessor<IncomingEvent> listener = new IncomingEventsProcessor<>(Context.getClient(),
				IncomingEventFactory::produce);

		listener.start(INCOMING_EVENTS_URL);
	}
}