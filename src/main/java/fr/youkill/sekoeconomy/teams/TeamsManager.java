package fr.youkill.sekoeconomy.teams;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.*;
import fr.youkill.sekoeconomy.database.DatabaseException;
import fr.youkill.sekoeconomy.database.DatabaseManager;
import fr.youkill.sekoeconomy.teams.request.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.logging.Logger;

@CommandAlias("seko-team")
public class TeamsManager extends BaseCommand  implements Listener {
    ArrayList<Team> teams;
    private final Logger logger;
    private final DatabaseManager database;
    private final PaperCommandManager commandManager;

    public TeamsManager(DatabaseManager databaseManager, Logger logger, PaperCommandManager commandManager) throws DatabaseException {
        this.commandManager = commandManager;
        this.database = databaseManager;
        this.logger = logger;

        this.updateTeams();
        this.displayCreatedTeams();

        commandManager.getCommandCompletions().registerAsyncCompletion("bddplayers", c -> {
            try {
                ArrayList<String> end = new ArrayList<String>();
                for (fr.youkill.sekoeconomy.teams.Player p : database.launchRequest(new GetPlayers()))
                    end.add(p.name);
                return end;
            } catch (DatabaseException e) {
                return new ArrayList<>();
            }
        });

        commandManager.getCommandCompletions().registerAsyncCompletion("bddteams", c -> {
            try {
                ArrayList<String> end = new ArrayList<String>();
                for (Team t : database.launchRequest(new GetTeams()))
                    end.add(t.name);
                return end;
            } catch (DatabaseException e) {
                return new ArrayList<>();
            }
        });
    }

    @Default
    public void onListTeam(Player player) {
        player.sendMessage("Teams :");
        for (Team team : this.teams) {
            player.sendMessage("Team: §2" + team.name + "§f");
            for (fr.youkill.sekoeconomy.teams.Player p : team.getPlayerList()) {
                player.sendMessage("-> §2" + p.name + "§f");
            }
        }
    }

    @Subcommand("players")
    public void onListPlayers(Player player) {
        try {
            ArrayList<fr.youkill.sekoeconomy.teams.Player> players = database.launchRequest(new GetPlayers());
            player.sendMessage("Players :");
            for  (fr.youkill.sekoeconomy.teams.Player p : players) {
                player.sendMessage("§2" + p.name + "§f");
            }
        } catch (DatabaseException e) {
            player.sendMessage("Can't get players -> " + e.getMessage());
        }
    }

    @Subcommand("create")
    @Syntax("[team-name]")
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
    @Syntax("[team-name]")
    @CommandCompletion("@bddteams")
    public void onDeleteTeam(Player player, String team_name) {
        try {
            this.database.launchRequest(new EmptyTeam(team_name));
            this.database.launchRequest(new DeleteTeam(team_name));
            this.updateTeams();
            player.sendMessage("Team destroyed !");
        } catch (DatabaseException e) {
            player.sendMessage("Can't delete team -> " + e.getMessage());
        }
    }

    @Subcommand("add-player")
    @CommandCompletion("@bddteams @bddplayers")
    @Syntax("[team-name] [player-name]")
    public void onAddPlayer(Player player, String team_name, String player_name) {
        try {
            fr.youkill.sekoeconomy.teams.Player p = getPlayerByName(player_name);
            database.launchRequest(new AddPlayerToTeam(p.uuid, team_name));
            this.updateTeams();
            player.sendMessage("Player added to the team !");
        } catch (DatabaseException e) {
            player.sendMessage("Can't add player -> " + e.getMessage());
        }
    }

    @Subcommand("remove-player")
    @Syntax("[team-name] [player-name]")
    @CommandCompletion("@bddteams @bddplayers")
    public void onRemovePlayer(Player player, String team_name, String player_name) {
        try {
            fr.youkill.sekoeconomy.teams.Player p = getPlayerByName(player_name);
            database.launchRequest(new RemovePlayerToTeam(p.uuid, team_name));
            this.updateTeams();
            player.sendMessage("Player removed of the team !");
        } catch (DatabaseException e) {
            player.sendMessage("Can't remove player -> " + e.getMessage());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            boolean exist = database.launchRequest(new PlayerExist(event.getPlayer().getUniqueId().toString()));
            if (!exist) {
                database.launchRequest(new CreatePlayer(event.getPlayer().getUniqueId().toString(), event.getPlayer().getName()));
                this.logger.info("New player found, added to bdd -> " + event.getPlayer().getName());
            }
        } catch (DatabaseException e) {
            this.logger.severe("Can't check if player is new -> " + e.getMessage());
        }
    }

    private void updateTeams() throws DatabaseException {
        this.teams = this.database.launchRequest(new GetTeams());
    }

    private void displayCreatedTeams() {
        for (Team team : this.teams) {
            logger.info("Team " + team.name + " created with players " + team.getPlayerList().toString());
        }
    }

    private fr.youkill.sekoeconomy.teams.Player getPlayerByName(String name) throws DatabaseException {
        ArrayList<fr.youkill.sekoeconomy.teams.Player> players = database.launchRequest(new GetPlayers());
        for (fr.youkill.sekoeconomy.teams.Player p : players) {
            if (p.name.equals(name))
                return p;
        }
        throw new DatabaseException("Can't find player " + name);
    }
}
