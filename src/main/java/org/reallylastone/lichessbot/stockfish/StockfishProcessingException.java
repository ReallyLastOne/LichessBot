package org.reallylastone.lichessbot.stockfish;

import java.io.IOException;

public class StockfishProcessingException extends RuntimeException {
	public StockfishProcessingException(String message, IOException ex) {
		super(message, ex);
	}
}
