package org.reallylastone.lichessbot;

import java.io.IOException;

import org.reallylastone.lichessbot.core.ChallengeManager;
import org.reallylastone.lichessbot.event.incoming.IncomingEventFactory;
import org.reallylastone.lichessbot.event.incoming.IncomingEventsProcessor;
import org.reallylastone.lichessbot.event.incoming.model.IncomingEvent;
import org.reallylastone.lichessbot.stockfish.StockfishRunner;
import org.reallylastone.lichessbot.stockfish.StockfishRunnerProxy;
import org.reallylastone.lichessbot.stockfish.command.GoMoveTimeCommand;
import org.reallylastone.lichessbot.stockfish.command.IsReadyCommand;
import org.reallylastone.lichessbot.utility.Context;

import static org.reallylastone.lichessbot.utility.Constants.URL.INCOMING_EVENTS_URL;

public class Main {
	public static void main(String[] args) throws IOException, InterruptedException {
		StockfishRunner runner = new StockfishRunnerProxy();
		runner.startEngine();
		System.out.println(runner.sendCommand(new IsReadyCommand()));
		System.out.println(runner.sendCommand(new GoMoveTimeCommand(1000)));
		 IncomingEventsProcessor<IncomingEvent> listener = new
				 IncomingEventsProcessor<>(Context.getClient(),
				IncomingEventFactory::produce);

		listener.start(INCOMING_EVENTS_URL);

		ChallengeManager manager = new ChallengeManager(Context.getMaxActiveChallengeHandlerStrategy());
		listener.subscribe(manager);
	}
}