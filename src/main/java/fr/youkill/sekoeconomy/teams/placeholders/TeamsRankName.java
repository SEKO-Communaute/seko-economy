package fr.youkill.sekoeconomy.teams.placeholders;

import fr.youkill.sekoeconomy.SekoEconomy;
import fr.youkill.sekoeconomy.teams.Team;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class TeamsRankName extends PlaceholderExpansion {
    private final SekoEconomy plugin;
    public TeamsRankName(SekoEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "sekoeconomy-teamatrank";
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
        ArrayList<Team> sortedTeams = this.plugin.teamsManager.getSortedTeams();
        try {
            int rank = Integer.parseInt(params);
            if (rank < 1 || rank > sortedTeams.size())
                return "#invalidranknumber";
            return sortedTeams.get(rank - 1).name;
        } catch (NumberFormatException e) {
            return "#invalidranknumber";
        }
    }
}
