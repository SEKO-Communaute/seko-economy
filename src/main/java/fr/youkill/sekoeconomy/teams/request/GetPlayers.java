package fr.youkill.sekoeconomy.teams.request;

import fr.youkill.sekoeconomy.database.DatabaseException;
import fr.youkill.sekoeconomy.database.requests.ADatabaseRequest;
import fr.youkill.sekoeconomy.teams.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetPlayers extends ADatabaseRequest<ArrayList<Player>> {
    @Override
    public RequestType getType() {
        return RequestType.QUERY;
    }

    @Override
    public @NotNull String getSQL() {
        return "SELECT player.name, player.id FROM player";
    }

    @Override
    public ArrayList<Player> convertResult(ResultSet set) throws DatabaseException {
        try {
            ArrayList<Player> players = new ArrayList<Player>();
            while (set.next()) {
                String playerName = set.getString("name");
                String playerUUID = set.getString("id");
                players.add(new Player(playerName, playerUUID));
            }
            return players;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
