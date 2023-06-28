package org.reallylastone.lichessbot;

import static org.reallylastone.lichessbot.utility.Constants.URL.INCOMING_EVENTS_URL;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.reallylastone.lichessbot.core.ChallengeManager;
import org.reallylastone.lichessbot.event.incoming.IncomingEventFactory;
import org.reallylastone.lichessbot.event.incoming.IncomingEventsProcessor;
import org.reallylastone.lichessbot.event.incoming.model.IncomingEvent;
import org.reallylastone.lichessbot.utility.Context;
import org.reallylastone.lichessbot.http.HttpUtil;

public class Main {
	public static void main(String[] args) throws IOException, InterruptedException {
		IncomingEventsProcessor<IncomingEvent> listener = new IncomingEventsProcessor<>(Context.getClient(),
				IncomingEventFactory::produce);

		listener.start(INCOMING_EVENTS_URL);

		ChallengeManager manager = new ChallengeManager(Context.getMaxActiveChallengeHandlerStrategy());
		listener.subscribe(manager);
	}
}