package org.reallylastone.lichessbot.event.game;

import static org.reallylastone.lichessbot.utility.Constants.URL.BOT_GAME_EVENTS_URL;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.reallylastone.lichessbot.http.HttpUtil;

public class GameEventsProcessor<T> extends SubmissionPublisher<T> implements Flow.Processor<String, T> {
	private final Logger logger = Logger.getLogger(GameEventsProcessor.class.getName());

	private final HttpClient client;
	private final Function<String, T> converter;
	private Flow.Subscription subscription;

	public GameEventsProcessor(HttpClient client, Function<String, T> converter) {
		this.client = client;
		this.converter = converter;
	}

	public void start(String gameId) {
		HttpRequest request = HttpUtil.authenticatedBuilder()
				.uri(URI.create(BOT_GAME_EVENTS_URL.replace("{gameId}", gameId)))
				.header("Content-Type", "application/x-ndjson").GET().build();

		new Thread(() -> client.sendAsync(request, HttpResponse.BodyHandlers.fromLineSubscriber(this))).start();
	}

	public void onSubscribe(Flow.Subscription subscription) {
		(this.subscription = subscription).request(1);
	}

	public void onNext(String item) {
		subscription.request(1);

		if (item == null || item.isEmpty() || item.isBlank()) {
			return;
		}

		T apply = converter.apply(item);
		logger.log(Level.INFO, () -> "Publishing: " + apply);
		submit(apply);
	}

	public void onError(Throwable ex) {
		closeExceptionally(ex);
	}

	public void onComplete() {
		close();
	}
}
