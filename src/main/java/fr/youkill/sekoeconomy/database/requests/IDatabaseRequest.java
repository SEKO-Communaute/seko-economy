package fr.youkill.sekoeconomy.database.requests;

import fr.youkill.sekoeconomy.database.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;

public interface IDatabaseRequest<T> {
    enum RequestType {
        QUERY,
        UPDATE
    }

    RequestType getType();
    @NotNull
    String getSQL();
    T convertResult(ResultSet set) throws DatabaseException;
    T convertUpdate(int result) throws DatabaseException;
}
