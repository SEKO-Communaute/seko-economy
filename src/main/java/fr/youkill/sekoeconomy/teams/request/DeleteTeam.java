package fr.youkill.sekoeconomy.teams.request;

import fr.youkill.sekoeconomy.database.requests.ADatabaseRequest;
import org.jetbrains.annotations.NotNull;

public class DeleteTeam extends ADatabaseRequest<Integer> {
    private final String team_name;

    public DeleteTeam(String team_name) {
        this.team_name = team_name;
    }

    @Override
    public RequestType getType() {
        return RequestType.UPDATE;
    }

    @Override
    public @NotNull String getSQL() {
        return "DELETE FROM team WHERE name = \"" + this.team_name + "\";";
    }
}
