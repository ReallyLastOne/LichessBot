package org.reallylastone.lichessbot.event;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Function;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reallylastone.lichessbot.http.HttpUtil;

public class GenericEventProcessor<T> extends SubmissionPublisher<T> implements Flow.Processor<String, T> {
	private final Logger logger = LogManager.getLogger(GenericEventProcessor.class.getName());

	private final HttpClient client;
	private final Function<String, T> converter;
	private Flow.Subscription subscription;

	public GenericEventProcessor(HttpClient client, Function<String, T> converter) {
		this.client = client;
		this.converter = converter;
	}

	public void start(String url) {
		HttpRequest request = HttpUtil.authenticatedBuilder().uri(URI.create(url))
				.header("Content-Type", "application/x-ndjson").GET().build();
		logger.log(Level.INFO, () -> "Starting listening on URL: " + url);
		try {
			client.sendAsync(request, HttpResponse.BodyHandlers.fromLineSubscriber(this));
		} catch (Exception e) {
			e.printStackTrace();
			// because after 1 hour of listening on api/stream/event, GOAWAY is received and stream is ended
			start(url);
		}
	}

	@Override
	public void onSubscribe(Flow.Subscription subscription) {
		this.subscription = subscription;
		subscription.request(1);
	}

	@Override
	public void onNext(String item) {
		subscription.request(1);
		if (item == null || item.isEmpty() || item.isBlank()) {
			return;
		}

		T apply = converter.apply(item);
		logger.log(Level.DEBUG, () -> "Publishing: " + apply);
		submit(apply);
	}

	@Override
	public void onError(Throwable ex) {
		logger.log(Level.ERROR, () -> "Exception in GenericEventProcessor: %s".formatted(ex.getMessage()), ex);
		ex.printStackTrace();

		closeExceptionally(ex);
	}

	@Override
	public void onComplete() {
		logger.log(Level.INFO, () -> "GenericEventProcessor complete");

		close();
	}
}
