package org.reallylastone.lichessbot.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.reallylastone.lichessbot.event.game.model.GameEvent;
import org.reallylastone.lichessbot.event.game.model.GameFull;
import org.reallylastone.lichessbot.event.game.model.GameState;
import org.reallylastone.lichessbot.event.game.model.White;
import org.reallylastone.lichessbot.http.HttpRequestSender;
import org.reallylastone.lichessbot.stockfish.StockfishRunner;
import org.reallylastone.lichessbot.stockfish.StockfishRunnerProxy;
import org.reallylastone.lichessbot.stockfish.command.GenericCommand;
import org.reallylastone.lichessbot.stockfish.command.GoMoveTimeCommand;


public class GameManager implements Flow.Subscriber<GameEvent> {
	private final Logger logger = Logger.getLogger(GameManager.class.getName());
	private final StockfishRunner stockfishRunner = new StockfishRunnerProxy();
	private Flow.Subscription subscription;
	private GameState currentState;
	private GameFull gameFull;

	@Override
	public void onSubscribe(Flow.Subscription subscription) {
		(this.subscription = subscription).request(1);
	}

	@Override
	public void onNext(GameEvent item) {
		subscription.request(1);
		logger.log(Level.INFO, () -> "Received: " + item);

		if (item instanceof GameFull full) {
			currentState = full.state;
			gameFull = full;
			boolean ownTurn = isOwnTurn(full.white, currentState.moves);
			try {
				stockfishRunner.startEngine();
				stockfishRunner.sendCommand(new GenericCommand("uci"));
				stockfishRunner.sendCommand(new GenericCommand("ucinewgame"));
				stockfishRunner.sendCommand(new GenericCommand("position startpos moves " + currentState.moves));
				if (ownTurn) {
					String stockfishLine = stockfishRunner.sendCommand(new GoMoveTimeCommand(1000));
					HttpRequestSender.makeMove(gameFull.id, stockfishLine.split(" ")[1]);
				}
			} catch (IOException | InterruptedException e) {
				throw new RuntimeException(e);
			}
		} else if (item instanceof GameState state) {
			currentState = state;
			boolean ownTurn = isOwnTurn(gameFull.white, currentState.moves);
			if (isGameFinished()) {
				stockfishRunner.stopEngine();
			}
			if (ownTurn) {
				stockfishRunner.sendCommand(new GenericCommand("position startpos moves " + currentState.moves));
				String stockfishLine = stockfishRunner.sendCommand(new GoMoveTimeCommand(1000));
				try {
					HttpRequestSender.makeMove(gameFull.id, stockfishLine.split(" ")[1]);
				} catch (IOException | InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	private boolean isGameFinished() {
		return List.of("mate", "resign", "outoftime").contains(currentState.status);
	}

	private boolean isOwnTurn(White white, String moves) {
		boolean evenNumberOfMoves = moves.equals("") || moves.split(" ").length % 2 == 0;

		return white.name.equals("GetFun") == evenNumberOfMoves;
	}

	@Override
	public void onError(Throwable ex) {
		logger.log(Level.SEVERE, () -> "Exception in GameManager: " + Arrays.toString(ex.getStackTrace()));
	}

	@Override
	public void onComplete() {
		logger.log(Level.INFO, () -> "GameManager complete");
	}

}
