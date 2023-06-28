package org.reallylastone.lichessbot.event.incoming;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Function;

import org.reallylastone.lichessbot.http.HttpUtil;

public class IncomingEventsProcessor<T> extends SubmissionPublisher<T> implements Flow.Processor<String, T> {

	private final HttpClient client;
	private final Function<String, T> converter;
	private Flow.Subscription subscription;

	public IncomingEventsProcessor(HttpClient client, Function<String, T> converter) {
		this.client = client;
		this.converter = converter;
	}

	public void start(String url) {
		HttpRequest request = HttpUtil.authenticatedBuilder().uri(URI.create(url))
				.header("Content-Type", "application/x-ndjson").GET().build();
		new Thread(() -> client.sendAsync(request, HttpResponse.BodyHandlers.fromLineSubscriber(this)).join()).start();
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
		submit(apply);
		System.out.println(apply);
	}

	public void onError(Throwable ex) {
		closeExceptionally(ex);
	}

	public void onComplete() {
		close();
	}
}
