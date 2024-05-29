package fr.youkill.sekoeconomy.tracking;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import fr.youkill.sekoeconomy.SekoEconomy;
import fr.youkill.sekoeconomy.database.DatabaseException;
import fr.youkill.sekoeconomy.teams.request.CreatePlayer;
import fr.youkill.sekoeconomy.tracking.request.CreateTransaction;
import fr.youkill.sekoeconomy.tracking.request.GetPlayerMoneyTransaction;
import fr.youkill.sekoeconomy.tracking.request.ResetTransactions;
import me.yic.xconomy.api.event.PlayerAccountEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

@CommandAlias("seko-track|strack")
public class EconomyTracking extends BaseCommand implements Listener {
    private final SekoEconomy plugin;
    private boolean isEnabled = true;
    private final MoneyCorrector moneyCorrector;

    public EconomyTracking(SekoEconomy plugin) {
        this.plugin = plugin;

        this.moneyCorrector = new MoneyCorrector(plugin);
        this.moneyCorrector.runTaskTimerAsynchronously(this.plugin, 0L, 10L * 60L * 20L); // Every 5 minutes
    }

    @Default
    public void checkTracking(Player p) {
        if (this.isEnabled)
            p.sendMessage("Tracking is §2Enabled§f");
        else
            p.sendMessage("Tracking is §eDisabled§f");
    }

    @Subcommand("set")
    @CommandCompletion("enable|disable")
    public void tracking(Player p, String state) {
        switch (state) {
            case "enable":
                p.sendMessage("Tracking is now §2enable§f");
                this.isEnabled = true;
                break;
            case "disable":
                p.sendMessage("Tracking is now §eDisable§f");
                this.isEnabled = false;
                break;
            default:
                p.sendMessage("§eFrerot, ta deux choix possible tu force§f");
        }
    }

    @Subcommand("find-error")
    public void findError(Player p) {
        p.sendMessage("Result of this command is displayed in the console");
        this.moneyCorrector.run();
    }

    @Subcommand("money")
    @CommandCompletion("@bddplayers")
    public void checkPlayerMoney(Player p, String player) {
        try {
            Double money = this.plugin.database.launchRequest(
                                new GetPlayerMoneyTransaction(Bukkit.getOfflinePlayer(player).getUniqueId().toString())
                            );
            p.sendMessage(player + " Have " + money);
        } catch (DatabaseException e) {
            p.sendMessage("Can't get money -> " + e.getMessage());
        }

    }

    @Subcommand("fixMoney")
    @CommandCompletion("*|@bddplayers")
    public void fixBalance(Player p, String player) {
        ArrayList<OfflinePlayer> players = new ArrayList<OfflinePlayer>();
        if (!player.equals("*")) {
            players.add(Bukkit.getOfflinePlayer(player));
        } else {
            this.plugin.teamsManager.getSortedTeams().forEach(
                    team -> team.getPlayerList().forEach(
                            current -> players.add(Bukkit.getOfflinePlayer(current.name))
                    )
            );
        }

        try {
            for (OfflinePlayer off_player : players) {
                Double money = this.plugin.database.launchRequest(
                        new GetPlayerMoneyTransaction(off_player.getUniqueId().toString())
                );
                fr.youkill.sekoeconomy.teams.Player my_player = new fr.youkill.sekoeconomy.teams.Player(
                        this.plugin, off_player.getName(), off_player.getUniqueId().toString()
                );
                if (my_player.getBalance().equals(money)) {
                    p.sendMessage(off_player.getName() + " -> No balance problem detected");
                } else {
                    this.plugin.database.launchRequest(new CreateTransaction(off_player.getUniqueId().toString(), my_player.getBalance() - money));
                    p.sendMessage(off_player.getName() + " -> Money fixed !");
                }
            }
        } catch (DatabaseException e) {
            p.sendMessage("Can't fix money -> " + e.getMessage());
        }
    }

    @Subcommand("resetTransactions")
    public void resetAll(Player p) {
        try {
            this.plugin.database.launchRequest(new ResetTransactions());
        } catch (DatabaseException e) {
            p.sendMessage("Can't reset transaction -> " + e.getMessage());
        }
    }

    @EventHandler
    private void listenPlayerAccount(PlayerAccountEvent event) {
        if (!this.isEnabled)
            return;
        if (event.getUniqueId() == UUID.fromString("2c5f00ba-902c-411b-b9b9-9261cf43aee1"))
            return; // Ignore tax account, I know it's hardcoded :(
        try {
            if (event.getisadd() == null) {
                Double player_amount = this.plugin.database.launchRequest(new GetPlayerMoneyTransaction(event.getUniqueId().toString()));
                this.plugin.database.launchRequest(new CreateTransaction(event.getUniqueId().toString(), event.getamount().doubleValue() - player_amount));
            } else if (event.getisadd()) {
                this.plugin.database.launchRequest(new CreateTransaction(event.getUniqueId().toString(), event.getamount().doubleValue()));
            }  else if (!event.getisadd()) {
                this.plugin.database.launchRequest(new CreateTransaction(event.getUniqueId().toString(), -event.getamount().doubleValue()));
            }
        } catch (DatabaseException e) {
            OfflinePlayer player = this.plugin.getServer().getOfflinePlayer(event.getUniqueId());
            this.plugin.getLogger().severe("Can't create transaction -> " + e.getMessage());
            this.plugin.getLogger().severe("Try to create the user");
            try {
                this.plugin.database.launchRequest(new CreatePlayer(player.getUniqueId().toString(), player.getName()));
            } catch (DatabaseException ex) {
                this.plugin.getLogger().severe("[CRITICAL !] Can't create the user !" + ex.getMessage());
            }

        }
    }
}
