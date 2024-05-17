package fr.youkill.sekoeconomy.database.requests;

import fr.youkill.sekoeconomy.database.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestGetTables extends ADatabaseRequest<String> {
    @Override
    public RequestType getType() {
        return RequestType.QUERY;
    }

    @Override
    public @NotNull String getSQL() {
        return "SHOW TABLES;";
    }

    @Override
    public String convertResult(ResultSet set) throws DatabaseException {
        StringBuilder sb = new StringBuilder();
        try {
            while (set.next()) {
                sb.append(set.getString(1)).append(",");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Can't convert result : " + e.getMessage());
        }
        return sb.toString();
    }
}
