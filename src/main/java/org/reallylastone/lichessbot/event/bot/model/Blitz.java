package org.reallylastone.lichessbot.event.bot.model;

public class Blitz{
    public int games;
    public int rating;
    public int rd;
    public int prog;
    public boolean prov;

    @Override
    public String toString() {
        return "Blitz{" +
                "games=" + games +
                ", rating=" + rating +
                ", rd=" + rd +
                ", prog=" + prog +
                ", prov=" + prov +
                '}';
    }
}