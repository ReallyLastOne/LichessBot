package org.reallylastone.lichessbot.utility;

import org.reallylastone.lichessbot.stockfish.StockfishRunnerProxy;

public class Preconditions {

	public void check() {
		Context.getBotName();
		Context.getToken();
		Context.getStockfishPath();
		new StockfishRunnerProxy().startEngine();
		new StockfishRunnerProxy().stopEngine();
	}
}
