package fr.youkill.sekoeconomy.tracking;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import fr.youkill.sekoeconomy.SekoEconomy;
import fr.youkill.sekoeconomy.database.DatabaseException;
import fr.youkill.sekoeconomy.tracking.request.CreateTransaction;
import fr.youkill.sekoeconomy.tracking.request.GetPlayerMoneyTransaction;
import me.yic.xconomy.api.event.PlayerAccountEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

@CommandAlias("seko-track|strack")
public class EconomyTracking extends BaseCommand implements Listener {
    private final SekoEconomy plugin;
    private boolean isEnabled = true;
    private final MoneyCorrector moneyCorrector;

    public EconomyTracking(SekoEconomy plugin) {
        this.plugin = plugin;

        this.moneyCorrector = new MoneyCorrector(plugin);
        this.moneyCorrector.runTaskTimerAsynchronously(this.plugin, 0L, 5L * 60L * 20L); // Every 5 minutes
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
    @CommandCompletion("@bddplayers")
    public void fixBalance(Player p, String player) {
        try {
            OfflinePlayer off_player = Bukkit.getOfflinePlayer(player);
            Double money = this.plugin.database.launchRequest(
                    new GetPlayerMoneyTransaction(off_player.getUniqueId().toString())
            );
            fr.youkill.sekoeconomy.teams.Player my_player = new fr.youkill.sekoeconomy.teams.Player(
                    this.plugin, off_player.getName(), off_player.getUniqueId().toString()
            );
            if (my_player.getBalance().equals(money)) {
                p.sendMessage("No balance problem detected");
            } else {
                this.plugin.database.launchRequest(new CreateTransaction(off_player.getUniqueId().toString(), my_player.getBalance() - money));
                p.sendMessage("Money fixed !");
            }

        } catch (DatabaseException e) {
            p.sendMessage("Can't fix money -> " + e.getMessage());
        }
    }

    @EventHandler
    private void listenPlayerAccount(PlayerAccountEvent event) {
        if (!this.isEnabled)
            return;
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
            this.plugin.getLogger().severe("Can't create transaction -> " + e.getMessage());
        }
    }
}
