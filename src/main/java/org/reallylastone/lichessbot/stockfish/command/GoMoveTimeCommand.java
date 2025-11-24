package org.reallylastone.lichessbot.stockfish.command;

import java.util.function.Predicate;

public class GoMoveTimeCommand implements StockfishCommand {
	private final long moveTime;

	public GoMoveTimeCommand(long moveTime) {
		this.moveTime = moveTime;
	}

	@Override
	public String getCLICommand() {
		return "go movetime " + moveTime;
	}

	@Override
	public Predicate<String> getTerminator() {
		return line -> line.contains("bestmove");
	}

	@Override
	public String toString() {
		return "GoMoveTimeCommand{" + "moveTime=" + moveTime + '}';
	}
}
