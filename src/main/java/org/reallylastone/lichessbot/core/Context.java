package org.reallylastone.lichessbot.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Properties;

public class Context {
	private static final HttpClient client = HttpClient.newHttpClient();
	private static String token = null;

	private Context() {
	}

	public static HttpClient getClient() {
		return client;
	}

	public static String getToken() {
		if (token != null) {
			return token;
		}

		String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		String appConfigPath = rootPath + "application.properties";

		Properties appProps = new Properties();
		try {
			appProps.load(new FileInputStream(appConfigPath));
		} catch (IOException e) {
			return null;
		}

		token = (String) appProps.get("token");

		return token;
	}
}
