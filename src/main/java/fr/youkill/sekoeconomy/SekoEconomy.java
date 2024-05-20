package fr.youkill.sekoeconomy;

import co.aikar.commands.PaperCommandManager;
import fr.youkill.sekoeconomy.database.DatabaseException;
import fr.youkill.sekoeconomy.database.DatabaseManager;
import fr.youkill.sekoeconomy.printer.CrazyPrinter;
import fr.youkill.sekoeconomy.teams.TeamsManager;
import fr.youkill.sekoeconomy.teams.placeholders.TeamsMoney;
import fr.youkill.sekoeconomy.teams.placeholders.TeamsRankMoney;
import fr.youkill.sekoeconomy.teams.placeholders.TeamsRankName;
import fr.youkill.sekoeconomy.tracking.EconomyTracking;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class SekoEconomy extends JavaPlugin {
    public CrazyPrinter printer = new CrazyPrinter(this);
    public Economy economy;
    public DatabaseManager database;
    public TeamsManager teamsManager;
    public EconomyTracking economyTracking;
    public PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        printer.printInConsole();
        this.saveDefaultConfig();
        commandManager = new PaperCommandManager(this);
        if (!setupVault() || !setupPlaceholder() || !setupDatabase() || !setupTeams() || !setupEconomy()) {
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
            database = new DatabaseManager(this);
            return true;
        } catch (DatabaseException e) {
            getLogger().severe(e.getMessage());
            return false;
        }
    }

    public boolean setupTeams() {
        try {
            teamsManager = new TeamsManager(this);
            new TeamsMoney(this).register();
            new TeamsRankName(this).register();
            new TeamsRankMoney(this).register();
            commandManager.registerCommand(teamsManager);
            getServer().getPluginManager().registerEvents(teamsManager, this);
            return true;
        } catch (DatabaseException e) {
            getLogger().severe(e.getMessage());
            return false;
        }
    }

    public boolean setupEconomy() {
        this.economyTracking = new EconomyTracking(this);
        commandManager.registerCommand(economyTracking);
        getServer().getPluginManager().registerEvents(economyTracking, this);
        return true;
    }
}
