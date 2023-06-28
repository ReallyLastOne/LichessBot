package org.reallylastone.lichessbot.http;

import static org.reallylastone.lichessbot.utility.Constants.URL.ACCEPT_CHALLENGE_URL;
import static org.reallylastone.lichessbot.utility.Constants.URL.MAKE_MOVE_URL;

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

	private static HttpRequest buildRequest(String url, HttpRequest.BodyPublisher bodyPublisher) {
		return HttpUtil.authenticatedBuilder().uri(URI.create(url)).POST(bodyPublisher).build();
	}

	private static HttpResponse<String> send(HttpRequest request) throws IOException, InterruptedException {
		return Context.getClient().send(request, HttpResponse.BodyHandlers.ofString());
	}
}
