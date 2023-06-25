package org.reallylastone.lichessbot.event.incoming.model;

public class GameStart extends IncomingEvent {
    public String gameId;
    public String fullId;
    public String color;
    public String fen;
    public boolean hasMoved;
    public boolean isMyTurn;
    public String lastMove;
    public Opponent opponent;
    public String perf;
    public boolean rated;
    public int secondsLeft;
    public String source;
    public Status status;
    public String speed;
    public Variant variant;
    public Compat compat;
    public String id;

    @Override
    public String toString() {
        return "GameStart{" +
                "gameId='" + gameId + '\'' +
                ", fullId='" + fullId + '\'' +
                ", color='" + color + '\'' +
                ", fen='" + fen + '\'' +
                ", hasMoved=" + hasMoved +
                ", isMyTurn=" + isMyTurn +
                ", lastMove='" + lastMove + '\'' +
                ", opponent=" + opponent +
                ", perf='" + perf + '\'' +
                ", rated=" + rated +
                ", secondsLeft=" + secondsLeft +
                ", source='" + source + '\'' +
                ", status=" + status +
                ", speed='" + speed + '\'' +
                ", variant=" + variant +
                ", compat=" + compat +
                ", id='" + id + '\'' +
                '}';
    }
}
