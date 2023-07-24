package org.reallylastone.lichessbot.stockfish;

import org.reallylastone.lichessbot.stockfish.command.StockfishCommand;

public interface StockfishRunner  {
	void startEngine();
	void stopEngine();
	String sendCommand(StockfishCommand command);
}
