package org.reallylastone.lichessbot.stockfish;

import java.io.IOException;

import org.reallylastone.lichessbot.stockfish.command.StockfishCommand;

public interface StockfishRunner  {
	void startEngine() throws IOException;
	void stopEngine();
	String sendCommand(StockfishCommand command);
}
