package org.reallylastone.lichessbot.stockfish.command;

import java.util.function.Predicate;

public interface StockfishCommand {
	String getCLICommand();

	Predicate<String> getTerminator();
}
