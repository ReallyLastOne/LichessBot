package org.reallylastone.lichessbot.utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.util.Properties;

import org.reallylastone.lichessbot.core.ChallengeHandlerStrategy;
import org.reallylastone.lichessbot.core.MaxActiveChallengeHandlerStrategy;

public class Context {
	private static final HttpClient client = HttpClient.newHttpClient();
	private static String token = null;
	private static String stockfishPath = null;

	private Context() {
	}

	public static HttpClient getClient() {
		return client;
	}

	public static String getToken() {
		return getProperty(token, "token");
	}

	public static String getStockfishPath() {
		if (stockfishPath == null) {
			String os = System.getProperty("os.name");

			if (os.contains("Windows")) {
				stockfishPath = ".\\runtime\\stockfish_15.1_win_x64_avx2\\stockfish-windows-2022-x86-64-avx2.exe";
			} else if (os.contains("Linux")) {
				stockfishPath = "../opt/app/runtime/stockfish/stockfish-ubuntu-x86-64-avx2";
			} else {
				throw new IllegalStateException("stockfish engine not supported for operating system " + os);
			}
		}

		return stockfishPath;
	}

	private static String getProperty(String classVariable, String property) {
		if (classVariable != null) {
			return classVariable;
		}

		InputStream rootPath = Context.class.getClassLoader().getResourceAsStream("application.properties");

		Properties appProps = new Properties();
		try {
			appProps.load(rootPath);
		} catch (IOException e) {
			return null;
		}

		classVariable = (String) appProps.get(property);

		return classVariable;
	}

	public static ChallengeHandlerStrategy getMaxActiveChallengeHandlerStrategy() {
		return new MaxActiveChallengeHandlerStrategy(1);
	}
}
