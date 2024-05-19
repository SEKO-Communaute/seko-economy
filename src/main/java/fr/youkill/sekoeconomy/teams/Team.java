package fr.youkill.sekoeconomy.teams;

import fr.youkill.sekoeconomy.SekoEconomy;

import java.util.ArrayList;

public class Team {
    private SekoEconomy plugin;
    public final String name;
    private final ArrayList<Player> playerList = new ArrayList<>();

    public Team(SekoEconomy plugin, String name) {
        this.plugin = plugin;
        this.name = name;
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

    public double getIngameBalance() {
        double endBalance = 0.0;
        for (Player p : this.playerList) {
            endBalance += p.getBalance();
        }
        return endBalance;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", playerList=" + playerList +
                '}';
    }
}
