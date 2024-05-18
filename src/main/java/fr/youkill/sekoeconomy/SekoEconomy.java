package fr.youkill.sekoeconomy;

import co.aikar.commands.PaperCommandManager;
import fr.youkill.sekoeconomy.database.DatabaseException;
import fr.youkill.sekoeconomy.database.DatabaseManager;
import fr.youkill.sekoeconomy.teams.TeamsManager;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class SekoEconomy extends JavaPlugin {
    private static Economy economy = null;
    private static DatabaseManager database;
    private static TeamsManager teamsManager;

    @Override
    public void onEnable() {
        if (!setupVault() || !setupPlaceholder() || !setupDatabase() || !setupTeams()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("All dependencies loaded !");
    }

    @Override
    public void onDisable() {
    }

    private boolean setupVault() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("Vault plugin not found !");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().severe("Vault economy addons not found !");
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    private boolean setupPlaceholder() {
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().severe("PlaceholderAPI not found !");
            return false;
        }
        return true;
    }

    private boolean setupDatabase() {
        try {
            database = new DatabaseManager();
            return true;
        } catch (DatabaseException e) {
            getLogger().severe(e.getMessage());
            return false;
        }
    }

    public boolean setupTeams() {
        PaperCommandManager commandManager = new PaperCommandManager(this);
        try {
            teamsManager = new TeamsManager(database, getLogger());
            commandManager.registerCommand(teamsManager);
            getServer().getPluginManager().registerEvents(teamsManager, this);
            return true;
        } catch (DatabaseException e) {
            getLogger().severe(e.getMessage());
            return false;
        }
    }

    public static Economy getEconomy() {
        return economy;
    }
}
