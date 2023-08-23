package org.reallylastone.lichessbot.http;

import static org.reallylastone.lichessbot.utility.Constants.URL.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reallylastone.lichessbot.utility.Context;

public class HttpRequestSender {
	private static final Logger logger = LogManager.getLogger(HttpRequestSender.class.getName());

	private HttpRequestSender() {
	}

	public static Optional<HttpResponse<String>> acceptChallenge(String id) {
		HttpRequest request = buildRequest(ACCEPT_CHALLENGE_URL.replace("{challengeId}", id),
				HttpRequest.BodyPublishers.noBody());

		return send(request);
	}

	public static Optional<HttpResponse<String>> makeMove(String gameId, String move) {
		HttpRequest request = buildRequest(MAKE_MOVE_URL.replace("{gameId}", gameId).replace("{move}", move),
				HttpRequest.BodyPublishers.ofString(""));

		return send(request);
	}

	public static Optional<HttpResponse<String>> cancelChallenge(String challengeId) {
		HttpRequest request = buildRequest(CANCEL_CHALLENGE.replace("{challengeId}", challengeId),
				HttpRequest.BodyPublishers.ofString(""));

		return send(request);
	}

	public static Optional<HttpResponse<String>> createChallenge(String form, String username) {
		HttpRequest request = buildRequest(CREATE_CHALLENGE.replace("{username}", username),
				HttpRequest.BodyPublishers.ofString(form), "Content-Type", "application/x-www-form-urlencoded");

		return send(request);
	}

	public static Optional<HttpResponse<String>> declineChallenge(String challengeId) {
		HttpRequest request = buildRequest(DECLINE_CHALLENGE.replace("{challengeId}", challengeId),
				HttpRequest.BodyPublishers.ofString(""));

		return send(request);
	}

	private static HttpRequest buildRequest(String url, HttpRequest.BodyPublisher bodyPublisher) {
		return builder(url, bodyPublisher).build();
	}

	private static HttpRequest buildRequest(String url, HttpRequest.BodyPublisher bodyPublisher, String... headers) {
		return builder(url, bodyPublisher).headers(headers).build();
	}

	private static HttpRequest.Builder builder(String url, HttpRequest.BodyPublisher bodyPublisher) {
		return HttpUtil.authenticatedBuilder().uri(URI.create(url)).POST(bodyPublisher);
	}

	private static Optional<HttpResponse<String>> send(HttpRequest request) {
		try {
			return Optional.of(Context.getClient().send(request, HttpResponse.BodyHandlers.ofString()));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			logger.log(Level.FATAL,
					() -> "error occurred during sending request %s with exception %s".formatted(request, e.getMessage()));
			return Optional.empty();
		}
	}
}
