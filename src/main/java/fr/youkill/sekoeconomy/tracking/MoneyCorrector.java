package fr.youkill.sekoeconomy.tracking;

import fr.youkill.sekoeconomy.SekoEconomy;
import fr.youkill.sekoeconomy.database.DatabaseException;
import fr.youkill.sekoeconomy.teams.Team;
import fr.youkill.sekoeconomy.tracking.request.GetPlayerMoneyTransaction;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MoneyCorrector extends BukkitRunnable {
    private final SekoEconomy plugin;
    public MoneyCorrector(SekoEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        this.plugin.getLogger().info("Check players balance...");
        AtomicInteger nbr_error = new AtomicInteger();
        ArrayList<Team> teams = this.plugin.teamsManager.getSortedTeams();
        teams.forEach((team) -> team.getPlayerList().forEach((player -> {
            try {
                Double databaseMoney = this.plugin.database.launchRequest(new GetPlayerMoneyTransaction(player.uuid));
                if (!player.getBalance().equals(databaseMoney)) {
                    this.plugin.getLogger().severe("Wrong balance for player " + player.name + " Ingame: " + player.getBalance()
                        + " transaction: " + databaseMoney);
                    nbr_error.addAndGet(1);
                }
            } catch (DatabaseException ignored) {}
        })));
        if (nbr_error.get() == 0) {
            this.plugin.getLogger().info("All player balances are correct");
        }
        else
            this.plugin.getLogger().severe("There are " + nbr_error.get() + " player balances incorrect");
    }
}
