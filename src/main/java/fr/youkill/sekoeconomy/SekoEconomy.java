package fr.youkill.sekoeconomy;

import fr.youkill.sekoeconomy.database.DatabaseException;
import fr.youkill.sekoeconomy.database.DatabaseManager;
import fr.youkill.sekoeconomy.database.requests.IDatabaseRequest;
import fr.youkill.sekoeconomy.database.requests.RequestGetTables;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class SekoEconomy extends JavaPlugin {
    private static Economy economy = null;
    private static DatabaseManager database;

    @Override
    public void onEnable() {
        if (!setupVault() || !setupPlaceholder() || !setupDatabase() ) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Plugin loaded");

        try {
            getLogger().info(database.launchRequest(new RequestGetTables()));
            IDatabaseRequest<String> test = new RequestGetTables();
            database.launchAsyncRequest(test, set -> {
                try {
                    System.out.println(test.convertResult(set));
                } catch (DatabaseException e) {
                    System.out.println("Error while exec request");
                }
            });
        } catch (DatabaseException e) {
            getLogger().severe(e.getMessage());
        }
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

    public static Economy getEconomy() {
        return economy;
    }
}
