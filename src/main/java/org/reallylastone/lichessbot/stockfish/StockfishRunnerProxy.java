package org.reallylastone.lichessbot.stockfish;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reallylastone.lichessbot.stockfish.command.StockfishCommand;

public class StockfishRunnerProxy implements StockfishRunner {
	private final Logger logger = LogManager.getLogger(StockfishRunnerProxy.class.getName());
	private final StockfishRunner stockfishRunner = new StockfishRunnerImpl();
	private final AtomicBoolean isRunning = new AtomicBoolean();

	@Override
	public void startEngine() {
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
