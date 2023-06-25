package org.reallylastone.lichessbot.event.incoming.model;

public class Status {
	public int id;
	public String name;

	@Override
	public String toString() {
		return "Status{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
