package fr.youkill.sekoeconomy.teams.request;

import fr.youkill.sekoeconomy.database.requests.ADatabaseRequest;
import org.jetbrains.annotations.NotNull;

public class RemovePlayerToTeam extends ADatabaseRequest<Integer> {
    private final String player_uuid;
    private final String team_name;
    public RemovePlayerToTeam(String player_uuid, String team_name) {
        this.player_uuid = player_uuid;
        this.team_name = team_name;
    }

    @Override
    public RequestType getType() {
        return RequestType.UPDATE;
    }

    @Override
    public @NotNull String getSQL() {
        return "DELETE FROM player_team WHERE id_player = \"" + this.player_uuid + "\" AND id_team = \"" + this.team_name + "\";";
    }
}
