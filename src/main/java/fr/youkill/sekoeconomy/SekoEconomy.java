package fr.youkill.sekoeconomy;

import net.milkbowl.vault.economy.Economy;

import org.apache.commons.lang.ObjectUtils;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class SekoEconomy extends JavaPlugin {
    private static Economy economy = null;

    @Override
    public void onEnable() {
        if (!setupVault() || !setupPlaceholder()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Plugin loaded");
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

    public static Economy getEconomy() {
        return economy;
    }
}
