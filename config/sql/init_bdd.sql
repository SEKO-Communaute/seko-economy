CREATE TABLE IF NOT EXISTS player (
                                      id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS team (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS player_team (
                                           id_player VARCHAR(36),
    id_team INT,
    PRIMARY KEY (id_player, id_team),
    FOREIGN KEY (id_player) REFERENCES player(id),
    FOREIGN KEY (id_team) REFERENCES team(id)
);

CREATE TABLE IF NOT EXISTS transaction (
                                           id INT AUTO_INCREMENT PRIMARY KEY,
                                           id_player VARCHAR(36),
    ammount DECIMAL(10,2),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_player) REFERENCES player(id)
);
