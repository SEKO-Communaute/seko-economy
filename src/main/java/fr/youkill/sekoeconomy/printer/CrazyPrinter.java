package fr.youkill.sekoeconomy.printer;

import fr.youkill.sekoeconomy.SekoEconomy;
import org.bukkit.ChatColor;
import org.bukkit.Color;

public class CrazyPrinter {
    private final SekoEconomy plugin;
    public CrazyPrinter(SekoEconomy plugin) {
        this.plugin = plugin;
    }

    public void printInConsole() {
        plugin.getLogger().info("\u001B[34m" + "Hello chui la !" + "\u001B[0m");
    }
}
