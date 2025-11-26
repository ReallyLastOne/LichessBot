package org.reallylastone.lichessbot.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ExitServerListener implements Runnable {
	public static final String EXIT_COMMAND = "EXIT";
	private static final Logger logger = LogManager.getLogger(ExitServerListener.class);
	private final int port;
	private volatile boolean exitSignalReceived;

	public ExitServerListener(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		try (ServerSocket server = new ServerSocket(port);
				Socket s = server.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()))) {

            String message = in.readLine();
            logger.info("Received info on TCP port {}", message);
            if (EXIT_COMMAND.equals(message)) {
				logger.info("EXIT signal socket received");
				exitSignalReceived = true;
			}
		} catch (Exception ignored) {
		}
	}

	public boolean isExitSignalReceived() {
		return exitSignalReceived;
	}
}
