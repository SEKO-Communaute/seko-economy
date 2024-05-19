package fr.youkill.sekoeconomy.teams;

import fr.youkill.sekoeconomy.SekoEconomy;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class Player {
    private SekoEconomy plugin;
    public final String name;
    public final String uuid;
    public Player(SekoEconomy plugin, String name, String uuid) {
        this.plugin = plugin;
        this.name = name;
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }

    public OfflinePlayer convertToBukkit() {
        return this.plugin.getServer().getOfflinePlayer(this.name);
    }
}
