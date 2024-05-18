package fr.youkill.sekoeconomy.teams.request;

import fr.youkill.sekoeconomy.database.requests.ADatabaseRequest;
import org.jetbrains.annotations.NotNull;
public class EmptyTeam extends ADatabaseRequest<Integer> {
    private final String team_name;
    public EmptyTeam(String team_name) {
        this.team_name = team_name;
    }

    @Override
    public RequestType getType() {
        return RequestType.UPDATE;
    }

    @Override
    public @NotNull String getSQL() {
        return "DELETE FROM player_team WHERE id_team = \"" + this.team_name + "\";";
    }
}
