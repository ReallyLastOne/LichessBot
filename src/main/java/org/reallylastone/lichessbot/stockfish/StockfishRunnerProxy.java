package org.reallylastone.lichessbot.stockfish;

import org.reallylastone.lichessbot.stockfish.command.StockfishCommand;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class StockfishRunnerProxy implements StockfishRunner {
	private final Logger logger = Logger.getLogger(StockfishRunnerProxy.class.getName());
	private final StockfishRunner stockfishRunner = new StockfishRunnerImpl();
	private final AtomicBoolean isRunning = new AtomicBoolean();

	@Override
	public void startEngine() throws IOException {
		if (!isRunning.getAndSet(true)) {
			stockfishRunner.startEngine();
		}
	}

	@Override
	public void stopEngine() {
		if (isRunning.getAndSet(false)) {
			stockfishRunner.stopEngine();
		}
	}

	@Override
	public String sendCommand(StockfishCommand command) {
		return stockfishRunner.sendCommand(command);
	}
}
