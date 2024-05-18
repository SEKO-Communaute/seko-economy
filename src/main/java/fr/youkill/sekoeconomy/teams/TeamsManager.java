package fr.youkill.sekoeconomy.teams;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import fr.youkill.sekoeconomy.database.DatabaseException;
import fr.youkill.sekoeconomy.database.DatabaseManager;
import fr.youkill.sekoeconomy.teams.request.CreateTeam;
import fr.youkill.sekoeconomy.teams.request.GetTeams;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.logging.Logger;

@CommandAlias("seko-team")
public class TeamsManager extends BaseCommand {
    ArrayList<Team> teams;
    private final DatabaseManager database;

    public TeamsManager(DatabaseManager databaseManager, Logger logger) throws DatabaseException {
        this.database = databaseManager;

        this.updateTeams();
        this.displayCreatedTeams(logger);
    }

    @Default
    public void onListTeam(Player player) {
        for (Team team : this.teams) {
            player.sendMessage("Team: §2" + team.name + "§f");
            for (fr.youkill.sekoeconomy.teams.Player p : team.getPlayerList()) {
                player.sendMessage("-> §2" + p.name + "§f");
            }
        }
    }

    @Subcommand("create")
    public void onCreateTeam(Player player, String name) {
        try {
            this.database.launchRequest(new CreateTeam(name));
            this.updateTeams();
            player.sendMessage("Team §2" + name + "§f created");
        } catch (DatabaseException e) {
            player.sendMessage("Can't create team -> " + e.getMessage());
        }
    }

    @Subcommand("delete")
    public void onDeleteTeam(Player player) {
        player.sendMessage("Tu delete");
    }

    private void updateTeams() throws DatabaseException {
        this.teams = this.database.launchRequest(new GetTeams());
    }

    private void displayCreatedTeams(Logger logger) {
        for (Team team : this.teams) {
            logger.info("Team " + team.name + " created with players " + team.getPlayerList().toString());
        }
    }
}
