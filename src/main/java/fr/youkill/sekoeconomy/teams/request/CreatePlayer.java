package fr.youkill.sekoeconomy.teams.request;

import fr.youkill.sekoeconomy.database.requests.ADatabaseRequest;
import org.jetbrains.annotations.NotNull;

public class CreatePlayer extends ADatabaseRequest<Integer> {
    private final String uuid;
    private final String name;
    public CreatePlayer(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public RequestType getType() {
        return RequestType.UPDATE;
    }

    @Override
    public @NotNull String getSQL() {
        return "INSERT INTO player (id, name) VALUES (\"" + this.uuid + "\", \"" + this.name + "\")";
    }
}
