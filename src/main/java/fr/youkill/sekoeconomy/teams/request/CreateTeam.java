package fr.youkill.sekoeconomy.teams.request;

import fr.youkill.sekoeconomy.database.DatabaseException;
import fr.youkill.sekoeconomy.database.requests.ADatabaseRequest;
import org.jetbrains.annotations.NotNull;

public class CreateTeam extends ADatabaseRequest<Integer> {
    private final String name;
    public CreateTeam(String name) {
        this.name = name;
    }

    @Override
    public RequestType getType() {
        return RequestType.UPDATE;
    }

    @Override
    public @NotNull String getSQL() {
        return "INSERT INTO team (name) VALUES (\"" + this.name + "\")";
    }
}
