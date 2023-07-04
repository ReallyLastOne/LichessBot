package org.reallylastone.lichessbot.stockfish.command;

import java.util.function.Predicate;

public class IsReadyCommand implements StockfishCommand {
	@Override
	public String getCLICommand() {
		return "isready";
	}

	@Override
	public Predicate<String> getTerminator() {
		return line -> line.contains("readyok");
	}
}
