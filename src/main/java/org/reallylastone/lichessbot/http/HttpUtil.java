package org.reallylastone.lichessbot.http;

import org.reallylastone.lichessbot.utility.Context;

import java.net.http.HttpRequest;

public class HttpUtil {
	public static HttpRequest.Builder authenticatedBuilder() {
		return HttpRequest.newBuilder().header("Authorization", "Bearer " + Context.getToken());
	}
}
