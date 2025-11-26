package org.reallylastone.lichessbot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reallylastone.lichessbot.core.ApplicationManager;
import org.reallylastone.lichessbot.core.DefaultChallengeHandlerStrategy;
import org.reallylastone.lichessbot.utility.ExitServerListener;
import org.reallylastone.lichessbot.utility.Preconditions;

public class Main {
	private static final Logger logger = LogManager.getLogger(Main.class.getName());

	static void main(String[] args) throws InterruptedException {
		logger.info("Starting application");
		new Preconditions().check();
		logger.info("All preconditions fulfilled");
        ExitServerListener exitServerListener = new ExitServerListener(9000);
		ApplicationManager manager = new ApplicationManager(new DefaultChallengeHandlerStrategy(1), exitServerListener);
        Thread.startVirtualThread(exitServerListener);
        Thread.currentThread().join();
	}
}