package org.reallylastone.lichessbot.event.incoming.model;

public class DestUser {
	public String id;
	public String name;
	public Object title;
	public int rating;
	public boolean provisional;
	public boolean online;
	public int lag;

	@Override
	public String toString() {
		return "DestUser{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", title=" + title + ", rating=" + rating
				+ ", provisional=" + provisional + ", online=" + online + ", lag=" + lag + '}';
	}
}
