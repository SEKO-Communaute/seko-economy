package fr.youkill.sekoeconomy.teams;

import java.util.ArrayList;

public class Team {
    public final String name;
    public final int id;
    private final ArrayList<Player> playerList = new ArrayList<>();

    public Team(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public void pushPlayer(Player player) {
        playerList.add(player);
    }

    public boolean containPlayer(String uuid) {
        return playerList.stream().anyMatch(player -> player.uuid.equals(uuid));
    }

    public ArrayList<Player> getPlayerList() {
        return this.playerList;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", playerList=" + playerList +
                '}';
    }
}
