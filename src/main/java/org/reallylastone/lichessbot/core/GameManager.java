package org.reallylastone.lichessbot.core;

import java.util.List;
import java.util.concurrent.Flow;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reallylastone.lichessbot.event.game.model.GameEvent;
import org.reallylastone.lichessbot.event.game.model.GameFull;
import org.reallylastone.lichessbot.event.game.model.GameState;
import org.reallylastone.lichessbot.event.game.model.White;
import org.reallylastone.lichessbot.http.HttpRequestSender;
import org.reallylastone.lichessbot.stockfish.StockfishRunner;
import org.reallylastone.lichessbot.stockfish.StockfishRunnerProxy;
import org.reallylastone.lichessbot.stockfish.command.GenericCommand;
import org.reallylastone.lichessbot.stockfish.command.GoMoveTimeCommand;
import org.reallylastone.lichessbot.utility.Context;

public class GameManager implements Flow.Subscriber<GameEvent> {
	private final Logger logger = LogManager.getLogger(GameManager.class.getName());
	private final StockfishRunner stockfishRunner = new StockfishRunnerProxy();
	private Flow.Subscription subscription;
	private GameState currentState;
	private GameFull fullGame;

	@Override
	public void onSubscribe(Flow.Subscription subscription) {
		(this.subscription = subscription).request(1);
	}

	@Override
	public void onNext(GameEvent item) {
		subscription.request(1);
		logger.log(Level.DEBUG, () -> "Received: " + item);

		switch (item) {
		case GameFull gameFull -> {
			logger.log(Level.INFO, () -> "Start of new game with id: %s".formatted(gameFull.id));
			onGameFull(gameFull);
		}
		case GameState gameState -> onGameState(gameState);
		default -> logger.log(Level.INFO, () -> "Got game event %s".formatted(item));
		}
	}

	@Override
	public void onError(Throwable ex) {
		logger.log(Level.ERROR, () -> "Exception in GameManager %s".formatted(ex.getMessage()), ex);
	}

	@Override
	public void onComplete() {
		logger.log(Level.INFO, () -> "GameManager complete");
	}

	private void onGameFull(GameFull gameFull) {
		this.fullGame = gameFull;
		currentState = fullGame.state;

		stockfishRunner.startEngine();
		stockfishRunner.sendCommand(new GenericCommand("uci"));
		stockfishRunner.sendCommand(new GenericCommand("ucinewgame"));

		handleOwnTurn();
	}

	private void onGameState(GameState gameState) {
		currentState = gameState;
		if (isGameFinished()) {
			logger.log(Level.INFO,
					() -> "game with id %s finished, status %s".formatted(fullGame.id, gameState.status));
			stockfishRunner.stopEngine();
		}

		handleOwnTurn();
	}

	private boolean isGameFinished() {
		return List.of("mate", "resign", "outoftime").contains(currentState.status);
	}

	private boolean isOwnTurn(White white, String moves) {
		boolean evenNumberOfMoves = moves.equals("") || moves.split(" ").length % 2 == 0;

		return white.name.equals(Context.getBotName()) == evenNumberOfMoves;
	}

	private void handleOwnTurn() {
		boolean ownTurn = isOwnTurn(fullGame.white, currentState.moves);
		if (ownTurn)
			onOwnTurn();
	}

	private void onOwnTurn() {
		stockfishRunner.sendCommand(new GenericCommand("position startpos moves " + currentState.moves));
		String stockfishLine = stockfishRunner.sendCommand(new GoMoveTimeCommand(1000));
		HttpRequestSender.makeMove(fullGame.id, stockfishLine.split(" ")[1]);
	}
}
