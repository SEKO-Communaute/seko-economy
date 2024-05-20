package fr.youkill.sekoeconomy.database;

import fr.youkill.sekoeconomy.SekoEconomy;
import fr.youkill.sekoeconomy.database.requests.IDatabaseRequest;

import java.sql.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class DatabaseManager {
    private Connection connection;
    private final DatabaseCrediential crediential;

    public DatabaseManager(SekoEconomy plugin) throws DatabaseException {
        crediential = new DatabaseCrediential(plugin);
        try {
            connection = DriverManager.getConnection(
                String.format("jdbc:mysql://%s:%s/%s", crediential.getIp(), crediential.getPort(), crediential.getDatabase()),
                crediential.getUser(),
                crediential.getPassword()
            );
        } catch (SQLException e) {
            throw new DatabaseException("Error while connecting to database : " + e.getMessage());
        }
    }

    public <T> T launchRequest(IDatabaseRequest<T> request) throws DatabaseException {
        try {
            Statement stmt = connection.createStatement();
            if (request.getType() == IDatabaseRequest.RequestType.QUERY) {
                ResultSet rs = stmt.executeQuery(request.getSQL());
                return request.convertResult(rs);
            } else {
                int rs = stmt.executeUpdate(request.getSQL());
                return request.convertUpdate(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while performing request : " + e.getMessage());
        }
    }

    public <T> void launchAsyncRequest(IDatabaseRequest<T> request) {
        CompletableFuture.runAsync(() -> {
            try {
                launchRequest(request);
            } catch (DatabaseException ignored) {}
        });
    }

    public <T> void launchAsyncRequest(IDatabaseRequest<T> request, Consumer<ResultSet> fallback) {
        CompletableFuture.supplyAsync(() -> {
            try {
                return launchRequest(request);
            } catch (DatabaseException e) {
                return new DatabaseException(e.getMessage());
            }
        }).thenAccept((ret) -> {
            if (!(ret instanceof DatabaseException))
                fallback.accept((ResultSet) ret);
        });
    }
}
