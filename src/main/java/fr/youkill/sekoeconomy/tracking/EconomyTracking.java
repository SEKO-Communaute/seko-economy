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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@CommandAlias("seko-track|strack")
public class EconomyTracking extends BaseCommand implements Listener {
    private final SekoEconomy plugin;
    private boolean isEnabled = true;

    public EconomyTracking(SekoEconomy plugin) {
        this.plugin = plugin;
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
