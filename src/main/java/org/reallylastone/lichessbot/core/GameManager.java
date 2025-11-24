package org.reallylastone.lichessbot.core;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reallylastone.lichessbot.stockfish.StockfishRunner;
import org.reallylastone.lichessbot.stockfish.StockfishRunnerProxy;
import org.reallylastone.lichessbot.stockfish.command.GenericCommand;
import org.reallylastone.lichessbot.stockfish.command.GoMoveTimeCommand;
import org.reallylastone.lichessbot.utility.Context;

import chariot.model.Event;
import chariot.model.GameStateEvent;

public class GameManager {
	public static final long MOVE_TIME = Duration.ofSeconds(1).toMillis();
	private final Logger logger = LogManager.getLogger(GameManager.class.getName());
	private final StockfishRunner stockfishRunner = new StockfishRunnerProxy();
	private final String gameId;
	private final Event.GameStartEvent gameStartEvent;
	private GameStateEvent.State currentState;
	private GameStateEvent.Full fullGame;

	public GameManager(Event.GameStartEvent gameStartEvent) {
		this.gameId = gameStartEvent.gameId();
		this.gameStartEvent = gameStartEvent;
		Thread.startVirtualThread(() -> {
			logger.info("Subscribing to a game {}", gameId);
			Context.auth().bot().connectToGame(gameId).stream().forEach(this::onNext);
		});
	}

	private void onNext(GameStateEvent item) {
		logger.debug("Game {} event received {} ", gameId, item);

		switch (item) {
		case GameStateEvent.Full gameFull -> {
			logger.info("Start of a new game {}", gameFull.id());
			onGameFull(gameFull);
		}
		case GameStateEvent.State gameState -> onGameState(gameState);
		default -> logger.error("Unknown event received {} in game {}", item, gameId);
		}
	}

	private void onGameFull(GameStateEvent.Full gameFull) {
		this.fullGame = gameFull;
		currentState = fullGame.state();

		stockfishRunner.startEngine();
		stockfishRunner.sendCommand(new GenericCommand("uci"));
		stockfishRunner.sendCommand(new GenericCommand("ucinewgame"));

		handleOwnTurn();
	}

	private void onGameState(GameStateEvent.State gameState) {
		currentState = gameState;
		if (isGameFinished()) {
			logger.info("Game {} finished, status {}", fullGame.id(), gameState.status().name());
			stockfishRunner.stopEngine();
		}

		handleOwnTurn();
	}

	private boolean isGameFinished() {
		return List.of("mate", "resign", "outoftime").contains(currentState.status().name());
	}

	private boolean isOwnTurn(GameStateEvent.Side white, String moves) {
		boolean evenNumberOfMoves = moves.isEmpty() || moves.split(" ").length % 2 == 0;

		return white.name().equals(Context.getBotName()) == evenNumberOfMoves;
	}

	private void handleOwnTurn() {
		boolean ownTurn = isOwnTurn(fullGame.white(), currentState.moves());
		if (ownTurn)
			onOwnTurn();
	}

	private void onOwnTurn() {
		stockfishRunner.sendCommand(new GenericCommand("position startpos moves " + currentState.moves()));
		String stockfishLine = stockfishRunner.sendCommand(new GoMoveTimeCommand(MOVE_TIME));
		Context.auth().bot().move(fullGame.id(), stockfishLine.split(" ")[1]);
	}
}
