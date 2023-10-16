package org.reallylastone.lichessbot.utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Supplier;

import org.reallylastone.lichessbot.core.ChallengeHandlerStrategy;
import org.reallylastone.lichessbot.core.DefaultChallengeHandlerStrategy;
import org.reallylastone.lichessbot.http.HttpRequestSender;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Context {
	public static final String TOKEN_ENV_NAME = "LICHESS_BOT_TOKEN";
	private static final HttpClient client = HttpClient.newHttpClient();
	private static final String tokenValue = System.getenv(TOKEN_ENV_NAME);
	private static String botName;
	private static String stockfishPath;

	private Context() {
	}

	public static HttpClient getClient() {
		return client;
	}

	public static String getToken() {
		return tokenValue;
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
			e.printStackTrace();
			return null;
		}

		classVariable = (String) appProps.get(property);

		return classVariable;
	}

	public static ChallengeHandlerStrategy getMaxActiveChallengeHandlerStrategy(Supplier<String> opponentSupplier) {
		return new DefaultChallengeHandlerStrategy(1, opponentSupplier);
	}

	public static String getBotName() {
		if (botName == null) {
			Optional<HttpResponse<String>> optional = HttpRequestSender.getMyProfile();
			var response = optional
					.orElseThrow(() -> new IllegalStateException("Could not fetch information about bot profile!"));
			JsonElement me = JsonParser.parseString(response.body());
			botName = me.getAsJsonObject().get("username").getAsString();
		}

		return botName;
	}
}
