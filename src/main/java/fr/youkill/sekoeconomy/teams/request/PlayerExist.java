package fr.youkill.sekoeconomy.teams.request;

import fr.youkill.sekoeconomy.database.DatabaseException;
import fr.youkill.sekoeconomy.database.requests.ADatabaseRequest;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerExist extends ADatabaseRequest<Boolean> {
    private final String uuid;

    public PlayerExist(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public RequestType getType() {
        return RequestType.QUERY;
    }

    @Override
    public @NotNull String getSQL() {
        return "SELECT COUNT(*) FROM player WHERE id = \"" + this.uuid + "\"";
    }

    @Override
    public Boolean convertResult(ResultSet set) throws DatabaseException {
        try {
            if (set.next()) {
                int count = set.getInt(1);
                return (count > 0);
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Can't check of user exist");
        }
    }
}
