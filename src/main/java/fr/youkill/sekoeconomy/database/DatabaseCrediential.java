package fr.youkill.sekoeconomy.database;

import fr.youkill.sekoeconomy.SekoEconomy;
import org.bukkit.configuration.file.FileConfiguration;

public class DatabaseCrediential {
    private String ip;
    private String user;
    private String password;
    private String database;
    private String port;

    DatabaseCrediential(SekoEconomy plugin) {
        FileConfiguration config = plugin.getConfig();

        ip = config.getString("database.ip");
        port = String.valueOf(config.getInt("database.port"));
        user = config.getString("database.user");
        password = config.getString("database.password");
        database = config.getString("database.database");
    }

    public String getIp() {
        return ip;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }

    public String getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "DatabaseCrediential{" +
                "ip='" + ip + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", database='" + database + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
