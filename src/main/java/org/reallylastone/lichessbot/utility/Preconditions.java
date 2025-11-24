package org.reallylastone.lichessbot.utility;

import org.reallylastone.lichessbot.stockfish.StockfishRunnerProxy;

public class Preconditions {

	public void check() {
		if (Context.getToken() == null) {
			throw new IllegalArgumentException(
					"Lichess token not provided. Please provide one in 'LICHESS_BOT_TOKEN' environmental variable");
		}

		if (Context.getBotName() == null) {
			throw new IllegalArgumentException("Could not fetch own Lichess profile");
		}

		Context.getStockfishPath();
		new StockfishRunnerProxy().startEngine();
		new StockfishRunnerProxy().stopEngine();
	}
}
