package org.reallylastone.lichessbot.stockfish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.reallylastone.lichessbot.stockfish.command.QuitCommand;
import org.reallylastone.lichessbot.stockfish.command.StockfishCommand;
import org.reallylastone.lichessbot.utility.Context;

class StockfishRunnerImpl implements StockfishRunner {
	private final Logger logger = Logger.getLogger(StockfishRunnerImpl.class.getName());
	private Process engineProcess;
	private BufferedReader processReader;
	private OutputStreamWriter processWriter;

	public void startEngine() {
		try {
			engineProcess = Runtime.getRuntime().exec(new String[] { Context.getStockfishPath() });
			processReader = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
			processWriter = new OutputStreamWriter(engineProcess.getOutputStream());
		} catch (IOException e) {
			throw new StockfishProcessingException(
					"could not start stockfish engine, exception %s".formatted(e.getMessage()), e);
		}
	}

	public String sendCommand(StockfishCommand command) {
		Predicate<String> commandDelimiter = command.getTerminator();

		try {
			logger.log(Level.FINEST, () -> "executing stockfish command %s".formatted(command));
			processWriter.write(command.getCLICommand() + System.lineSeparator());
			processWriter.flush();
			return commandDelimiter == null ? null : getCommandOutput(commandDelimiter);
		} catch (IOException e) {
			throw new StockfishProcessingException(
					"error occurred during writing command to process %s".formatted(command), e);
		}
	}

	private String getCommandOutput(Predicate<String> commandDelimiter) throws IOException {
		while (true) {
			String line = processReader.readLine();
			if (commandDelimiter.test(line)) {
				return line;
			}
		}
	}

	public void stopEngine() {
		try {
			logger.log(Level.INFO, "stopping stockfish engine...");
			sendCommand(new QuitCommand());
			processReader.close();
			processWriter.close();
		} catch (IOException e) {
			throw new StockfishProcessingException("error occurred during closing engine processes", e);
		}
	}
}
