package fr.youkill.sekoeconomy.database.requests;

import fr.youkill.sekoeconomy.database.DatabaseException;

import java.sql.ResultSet;

public abstract class ADatabaseRequest<T> implements IDatabaseRequest<T> {
    @Override
    public T convertResult(ResultSet set) throws DatabaseException {
        return null;
    }

    @Override
    public T convertUpdate(int result) throws DatabaseException {
        return null;
    }
}
