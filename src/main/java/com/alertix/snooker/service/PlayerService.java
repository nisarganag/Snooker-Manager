package com.alertix.snooker.service;

import java.util.List;
import java.util.Optional;

import com.alertix.snooker.domain.Player;

public interface PlayerService {
    List<Player> getAllPlayers();

    Optional<Player> getPlayerById(Long id);

    Player createPlayer(Player player);

    Player updatePlayer(Long id, Player player);

    void deletePlayer(Long id);
}
