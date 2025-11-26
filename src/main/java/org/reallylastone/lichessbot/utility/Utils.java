package org.reallylastone.lichessbot.utility;

import chariot.model.Event;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public class Utils {

	public static boolean isOwnChallengePresent(Map<ZonedDateTime, Event.ChallengeCreatedEvent> activeChallenges) {
		return !findActiveChallenges(activeChallenges).isEmpty();
	}

	public static List<Event.ChallengeCreatedEvent> findActiveChallenges(
			Map<ZonedDateTime, Event.ChallengeCreatedEvent> activeChallenges) {
		return activeChallenges.values().stream().filter(e -> e.challenge().players().challengerOpt().stream()
				.anyMatch(f -> f.user().name().equals(Context.getBotName()))).toList();
	}
}
