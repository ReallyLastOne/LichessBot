package org.reallylastone.lichessbot.stockfish.command;

import java.util.function.Predicate;

public class QuitCommand implements StockfishCommand {
	@Override
	public String getCLICommand() {
		return "quit";
	}

	@Override
	public Predicate<String> getTerminator() {
		return line -> true;
	}
}
