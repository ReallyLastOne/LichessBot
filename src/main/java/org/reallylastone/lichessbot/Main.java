package org.reallylastone.lichessbot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reallylastone.lichessbot.core.ApplicationManager;
import org.reallylastone.lichessbot.core.DefaultChallengeHandlerStrategy;
import org.reallylastone.lichessbot.utility.Preconditions;

public class Main {
	private static final Logger logger = LogManager.getLogger(Main.class.getName());

	static void main(String[] args) throws InterruptedException {
		logger.info("Starting application");
		new Preconditions().check();

		ApplicationManager manager = new ApplicationManager(new DefaultChallengeHandlerStrategy(1));
		Thread.currentThread().join();
	}
}