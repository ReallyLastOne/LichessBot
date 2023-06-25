package org.reallylastone.lichessbot.event.incoming.model;

import com.google.gson.annotations.SerializedName;

public class Variant {
	public String key;
	public String name;
	@SerializedName("short")
	public String myshort;

	@Override
	public String toString() {
		return "Variant{" +
				"key='" + key + '\'' +
				", name='" + name + '\'' +
				", myshort='" + myshort + '\'' +
				'}';
	}
}
