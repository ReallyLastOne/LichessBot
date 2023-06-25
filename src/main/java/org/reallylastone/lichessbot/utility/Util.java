package org.reallylastone.lichessbot.utility;

import java.net.http.HttpRequest;

public class Util {
	public static HttpRequest.Builder authenticatedBuilder() {
		return HttpRequest.newBuilder().header("Authorization", "Bearer " + Context.getToken());
	}
}
