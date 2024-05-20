package fr.youkill.sekoeconomy.tracking.request;

import fr.youkill.sekoeconomy.database.requests.ADatabaseRequest;
import org.jetbrains.annotations.NotNull;

public class ResetTransactions extends ADatabaseRequest<Integer> {
    @Override
    public RequestType getType() {
        return RequestType.UPDATE;
    }

    @Override
    public @NotNull String getSQL() {
        return "TRUNCATE TABLE transaction;";
    }
}
