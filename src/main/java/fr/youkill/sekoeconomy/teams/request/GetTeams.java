package fr.youkill.sekoeconomy.teams.request;

import fr.youkill.sekoeconomy.SekoEconomy;
import fr.youkill.sekoeconomy.database.DatabaseException;
import fr.youkill.sekoeconomy.database.requests.ADatabaseRequest;
import fr.youkill.sekoeconomy.teams.Player;
import fr.youkill.sekoeconomy.teams.Team;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class GetTeams extends ADatabaseRequest<ArrayList<Team>> {
    private SekoEconomy plugin;
    public GetTeams(SekoEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public RequestType getType() {
        return RequestType.QUERY;
    }

    @Override
    public @NotNull String getSQL() {
        return "SELECT team.name AS team_name,\n" +
                "       GROUP_CONCAT(CONCAT(player.name, ':', player.id) SEPARATOR ',') AS players\n" +
                "FROM team\n" +
                "         LEFT JOIN player_team ON team.name = player_team.id_team\n" +
                "         LEFT JOIN player ON player_team.id_player = player.id\n" +
                "GROUP BY team.name";
    }

    @Override
    public ArrayList<Team> convertResult(ResultSet set) throws DatabaseException {
        ArrayList<Team> end = new ArrayList<Team>();
        try {
            while (set.next()) {
                String team_name = set.getString("team_name");
                String players = set.getString("players");

                Team current = new Team(this.plugin, team_name);
                end.add(current);
                if (players == null || players.isEmpty())
                    continue;
                String[] playerArray = players.split(",");
                for (String player : playerArray) {
                    String[] playerInfo = player.split(":");
                    String playerName = playerInfo[0];
                    String playerUUID = playerInfo[1];
                    Player newPlayer = new Player(this.plugin, playerName, playerUUID);
                    current.pushPlayer(newPlayer);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return end;
    }
}
