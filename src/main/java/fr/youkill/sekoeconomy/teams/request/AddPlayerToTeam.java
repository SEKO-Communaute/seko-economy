package fr.youkill.sekoeconomy.teams.request;

import fr.youkill.sekoeconomy.database.requests.ADatabaseRequest;
import org.jetbrains.annotations.NotNull;

public class AddPlayerToTeam extends ADatabaseRequest<Integer> {
    private final String player_uuid;
    private final String team_name;
    public AddPlayerToTeam(String player_uuid, String team_name) {
        this.player_uuid = player_uuid;
        this.team_name = team_name;
    }

    @Override
    public RequestType getType() {
        return RequestType.UPDATE;
    }

    @Override
    public @NotNull String getSQL() {
        return "INSERT INTO player_team (id_player, id_team) VALUES (\"" + this.player_uuid + "\",\"" + this.team_name + "\");";
    }
}
