package fr.youkill.sekoeconomy.tracking.request;

import fr.youkill.sekoeconomy.database.DatabaseException;
import fr.youkill.sekoeconomy.database.requests.ADatabaseRequest;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GetPlayerMoneyTransaction extends ADatabaseRequest<Double> {
    private final String player_id;
    public GetPlayerMoneyTransaction(String player_id) {
        this.player_id = player_id;
    }

    @Override
    public RequestType getType() {
        return RequestType.QUERY;
    }

    @Override
    public @NotNull String getSQL() {
        return "SELECT SUM(ammount) AS balance\n" +
                "FROM transaction\n" +
                "WHERE id_player = \"" + this.player_id + "\";";
    }

    @Override
    public Double convertResult(ResultSet set) throws DatabaseException {
        try {
            if (set.next()) {
                return set.getDouble("balance");
            } else {
                return 0.0;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
