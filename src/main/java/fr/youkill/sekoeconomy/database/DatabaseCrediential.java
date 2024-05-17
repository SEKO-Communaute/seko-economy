package fr.youkill.sekoeconomy.database;

public class DatabaseCrediential {
    private String ip;
    private String user;
    private String password;
    private String database;
    private String port;

    DatabaseCrediential() {
        ip = "sql3.minestrator.com";
        port = "3306";
        user = "minesr_bw2LdGg4";
        password = "bNt4pqLQjjBlgMc2";
        database = "minesr_bw2LdGg4";
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
