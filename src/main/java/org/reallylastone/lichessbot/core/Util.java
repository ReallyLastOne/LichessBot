package org.reallylastone.lichessbot.core;

import java.net.http.HttpRequest;

public class Util {
	public static HttpRequest.Builder authenticatedBuilder() {
		return HttpRequest.newBuilder().header("Authorization", "Bearer " + Context.getToken());
	}
}
