package fr.youkill.sekoeconomy.teams;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.youkill.sekoeconomy.SekoEconomy;
import fr.youkill.sekoeconomy.database.DatabaseException;
import fr.youkill.sekoeconomy.teams.request.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@CommandAlias("seko-team|steam")
public class TeamsManager extends BaseCommand  implements Listener {
    ArrayList<Team> teams;
    private final SekoEconomy plugin;

    public TeamsManager(SekoEconomy plugin) throws DatabaseException {
        this.plugin = plugin;
        this.updateTeams();
        this.displayCreatedTeams();

        plugin.commandManager.getCommandCompletions().registerAsyncCompletion("bddplayers", c -> {
            try {
                ArrayList<String> end = new ArrayList<String>();
                for (fr.youkill.sekoeconomy.teams.Player p : plugin.database.launchRequest(new GetPlayers(this.plugin)))
                    end.add(p.name);
                return end;
            } catch (DatabaseException e) {
                return new ArrayList<>();
            }
        });

        plugin.commandManager.getCommandCompletions().registerAsyncCompletion("bddteams", c -> {
            try {
                ArrayList<String> end = new ArrayList<String>();
                for (Team t : plugin.database.launchRequest(new GetTeams(this.plugin)))
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
            ArrayList<fr.youkill.sekoeconomy.teams.Player> players = plugin.database.launchRequest(new GetPlayers(this.plugin));
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
            plugin.database.launchRequest(new CreateTeam(name));
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
            plugin.database.launchRequest(new EmptyTeam(team_name));
            plugin.database.launchRequest(new DeleteTeam(team_name));
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
            plugin.database.launchRequest(new AddPlayerToTeam(p.uuid, team_name));
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
            plugin.database.launchRequest(new RemovePlayerToTeam(p.uuid, team_name));
            this.updateTeams();
            player.sendMessage("Player removed of the team !");
        } catch (DatabaseException e) {
            player.sendMessage("Can't remove player -> " + e.getMessage());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            boolean exist = plugin.database.launchRequest(new PlayerExist(event.getPlayer().getUniqueId().toString()));
            if (!exist) {
                plugin.database.launchRequest(new CreatePlayer(event.getPlayer().getUniqueId().toString(), event.getPlayer().getName()));
                plugin.getLogger().info("New player found, added to bdd -> " + event.getPlayer().getName());
            }
        } catch (DatabaseException e) {
            plugin.getLogger().severe("Can't check if player is new -> " + e.getMessage());
        }
    }

    private void updateTeams() throws DatabaseException {
        this.teams = plugin.database.launchRequest(new GetTeams(this.plugin));
    }

    private void displayCreatedTeams() {
        for (Team team : this.teams) {
            plugin.getLogger().info("Team " + team.name + " created with players " + team.getPlayerList().toString());
        }
    }

    private fr.youkill.sekoeconomy.teams.Player getPlayerByName(String name) throws DatabaseException {
        ArrayList<fr.youkill.sekoeconomy.teams.Player> players = plugin.database.launchRequest(new GetPlayers(this.plugin));
        for (fr.youkill.sekoeconomy.teams.Player p : players) {
            if (p.name.equals(name))
                return p;
        }
        throw new DatabaseException("Can't find player " + name);
    }

    public Team getTeamByName(@NotNull String name) {
        for (Team team : this.teams) {
            if (team.name.equals(name))
                return team;
        }
        return null;
    }

    public ArrayList<Team> getSortedTeams() {
        ArrayList<Team> sortedTeams = new ArrayList<>(teams);
        sortedTeams.sort(
            (t1, t2) -> Double.compare(t2.getIngameBalance(), t1.getIngameBalance())
        );
        return sortedTeams;
    }
}
