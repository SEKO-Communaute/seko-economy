package fr.youkill.sekoeconomy.teams;

public class Player {
    public final String name;
    public final String uuid;
    public Player(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
