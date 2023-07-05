package org.reallylastone.lichessbot.http;

import static org.reallylastone.lichessbot.utility.Constants.URL.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.reallylastone.lichessbot.utility.Context;

public class HttpRequestSender {

	public static HttpResponse<String> acceptChallenge(String id) throws IOException, InterruptedException {
		HttpRequest request = buildRequest(ACCEPT_CHALLENGE_URL.replace("{challengeId}", id),
				HttpRequest.BodyPublishers.noBody());

		return send(request);
	}

	public static HttpResponse<String> makeMove(String gameId, String move) throws IOException, InterruptedException {
		HttpRequest request = buildRequest(MAKE_MOVE_URL.replace("{gameId}", gameId).replace("{move}", move),
				HttpRequest.BodyPublishers.ofString(""));

		return send(request);
	}

	public static HttpResponse<String> cancelChallenge(String challengeId) throws IOException, InterruptedException {
		HttpRequest request = buildRequest(CANCEL_CHALLENGE.replace("{challengeId}", challengeId),
				HttpRequest.BodyPublishers.ofString(""));

		return send(request);
	}

	public static HttpResponse<String> createChallenge(String form, String username)
			throws IOException, InterruptedException {
		HttpRequest request = buildRequest(CREATE_CHALLENGE.replace("{username}", username),
				HttpRequest.BodyPublishers.ofString(form), "Content-Type", "application/x-www-form-urlencoded");

		return send(request);
	}

	public static HttpResponse<String> declineChallenge(String challengeId) throws IOException, InterruptedException {
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

	private static HttpResponse<String> send(HttpRequest request) throws IOException, InterruptedException {
		return Context.getClient().send(request, HttpResponse.BodyHandlers.ofString());
	}
}
