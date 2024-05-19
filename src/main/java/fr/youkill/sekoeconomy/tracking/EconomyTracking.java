package fr.youkill.sekoeconomy.tracking;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import fr.youkill.sekoeconomy.SekoEconomy;
import org.bukkit.entity.Player;

@CommandAlias("seko-track|strack")
public class EconomyTracking extends BaseCommand {
    private final SekoEconomy plugin;
    public EconomyTracking(SekoEconomy plugin) {
        this.plugin = plugin;
    }

    @Default
    public void checkTracking(Player p) {
        p.sendMessage("Chez pas bro c pas codé");
    }

    @Subcommand("set")
    @CommandCompletion("enable|disable")
    public void tracking(Player p, String state) {
        if (!state.equals("enable") && !state.equals("disable"))
            p.sendMessage("bro tu force");
        else
            p.sendMessage("ok");
    }
}
