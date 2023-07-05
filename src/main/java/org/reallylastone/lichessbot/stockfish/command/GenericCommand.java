package org.reallylastone.lichessbot.stockfish.command;

import java.util.function.Predicate;

public class GenericCommand implements StockfishCommand {
	private final String command;

	public GenericCommand(String command) {
		this.command = command;
	}

	@Override
	public String getCLICommand() {
		return command;
	}

	@Override
	public Predicate<String> getTerminator() {
		return null;
	}
}
