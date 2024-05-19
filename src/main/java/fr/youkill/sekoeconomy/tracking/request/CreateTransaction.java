package fr.youkill.sekoeconomy.tracking.request;

import fr.youkill.sekoeconomy.database.requests.ADatabaseRequest;
import org.jetbrains.annotations.NotNull;

public class CreateTransaction extends ADatabaseRequest<Integer> {
    private final String player_id;
    private final double amount;

    public CreateTransaction(String player_id, double amount) {
        this.player_id = player_id;
        this.amount = amount;
    }


    @Override
    public RequestType getType() {
        return RequestType.UPDATE;
    }

    @Override
    public @NotNull String getSQL() {
        return "INSERT INTO transaction (id_player, ammount) VALUES (\"" + this.player_id + "\", " + amount + ");";
    }
}
