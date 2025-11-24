package org.reallylastone.lichessbot.utility;

import chariot.Client;
import chariot.ClientAuth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Properties;

public class Context {

	public static final String TOKEN_ENV_NAME = "LICHESS_BOT_TOKEN";
	private static final String tokenValue = System.getenv(TOKEN_ENV_NAME);
	private static final ClientAuth auth = Client.auth(Context.getToken());
	private static final Logger logger = LogManager.getLogger(Context.class.getName());
	/**
	 * Lichess lets us send only one request per second, so pause sometimes to
	 * comply
	 */
	private static final Duration REQUEST_INTERVAL = Duration.ofSeconds(1);
	private static final Client basic = Client.basic();
	private static String botName;
	private static String stockfishPath;
	private static ZonedDateTime lastRequestTime;

	private Context() {
	}

	public static String getToken() {
		return tokenValue;
	}

	public static String getStockfishPath() {
		if (stockfishPath == null) {
			String os = System.getProperty("os.name");

            // TODO: move it to application.properties file
			if (os.contains("Windows")) {
				stockfishPath = ".\\runtime\\stockfish_windows\\stockfish-windows-x86-64-avx2.exe";
			} else if (os.contains("Linux")) {
				stockfishPath = "../opt/app/runtime/stockfish_linux/stockfish-ubuntu-x86-64-avx2";
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
			logger.error("Error when reading application properties", e);
			return null;
		}

		classVariable = (String) appProps.get(property);

		return classVariable;
	}

	public static String getBotName() {
		if (botName == null) {
			botName = Context.auth().account().profile().get().name();
			logger.info("Playing as a {}", botName);
		}

		return botName;
	}

	public static ClientAuth auth() {
		waitForRateLimit();
		lastRequestTime = ZonedDateTime.now();

		return auth;
	}

	public static Client basic() {
		waitForRateLimit();
		lastRequestTime = ZonedDateTime.now();

		return basic;
	}

	private static void waitForRateLimit() {
		if (lastRequestTime != null) {
			long difference = Duration.between(lastRequestTime, ZonedDateTime.now()).toMillis();
			if (difference < REQUEST_INTERVAL.toMillis()) {
				try {
					long sleepTime = REQUEST_INTERVAL.toMillis() - difference;
					logger.trace("Too many requests! Sleeping for {} millis", sleepTime);
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
