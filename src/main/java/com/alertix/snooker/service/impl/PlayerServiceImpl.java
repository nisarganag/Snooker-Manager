package com.alertix.snooker.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alertix.snooker.domain.Player;
import com.alertix.snooker.repository.PlayerRepository;
import com.alertix.snooker.service.PlayerService;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Optional<Player> getPlayerById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return playerRepository.findById(id);
    }

    @Override
    public Player createPlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        return playerRepository.save(player);
    }

    @Override
    public Player updatePlayer(Long id, Player playerDetails) {
        if (id == null) {
            throw new IllegalArgumentException("Player ID cannot be null");
        }
        if (playerDetails == null) {
            throw new IllegalArgumentException("Player details cannot be null");
        }
        return playerRepository.findById(id).map(player -> {
            player.setName(playerDetails.getName());
            player.setNationality(playerDetails.getNationality());
            return playerRepository.save(player);
        }).orElseThrow(() -> new RuntimeException("Player not found"));
    }

    @Override
    public void deletePlayer(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Player ID cannot be null");
        }
        playerRepository.deleteById(id);
    }
}
