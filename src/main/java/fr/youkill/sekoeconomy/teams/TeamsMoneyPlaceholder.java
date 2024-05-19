package fr.youkill.sekoeconomy.teams;

import fr.youkill.sekoeconomy.SekoEconomy;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TeamsMoneyPlaceholder extends PlaceholderExpansion {
    private final SekoEconomy plugin;
    public TeamsMoneyPlaceholder(SekoEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "sekoeconomy-teammoney";
    }

    @Override
    public @NotNull String getAuthor() {
        return "You_Kill";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        Team t = this.plugin.teamsManager.getTeamByName(params);
        if (t == null)
            return "#team-" + params + "-notfound";
        return ((Double) t.getIngameBalance()).toString();
    }
}
