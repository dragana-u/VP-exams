package mk.ukim.finki.wp.kol2023.g1.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.wp.kol2023.g1.model.Player;
import mk.ukim.finki.wp.kol2023.g1.model.PlayerPosition;
import mk.ukim.finki.wp.kol2023.g1.model.exceptions.InvalidPlayerIdException;
import mk.ukim.finki.wp.kol2023.g1.repository.PlayerRepository;
import mk.ukim.finki.wp.kol2023.g1.service.PlayerService;
import mk.ukim.finki.wp.kol2023.g1.service.TeamService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static mk.ukim.finki.wp.kol2023.g1.service.FieldFilterSpecification.*;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamService teamService;

    @Override
    public List<Player> listAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Player findById(Long id) {
        return playerRepository.findById(id).orElseThrow(InvalidPlayerIdException::new);
    }

    @Override
    public Player create(String name, String bio, Double pointsPerGame, PlayerPosition position, Long team) {
        return playerRepository.save(new Player(name,bio,pointsPerGame,position,teamService.findById(team)));
    }

    @Override
    public Player update(Long id, String name, String bio, Double pointsPerGame, PlayerPosition position, Long team) {
        Player player = this.findById(id);
        player.setName(name);
        player.setBio(bio);
        player.setPointsPerGame(pointsPerGame);
        player.setPosition(position);
        player.setTeam(teamService.findById(team));
        return playerRepository.save(player);
    }

    @Override
    public Player delete(Long id) {
        Player player = this.findById(id);
        playerRepository.delete(player);
        return player;
    }

    @Override
    public Player vote(Long id) {
        Player player = this.findById(id);
        player.setVotes(player.getVotes() + 1);
        playerRepository.save(player);
        return player;
    }

    @Override
    public List<Player> listPlayersWithPointsLessThanAndPosition(Double pointsPerGame, PlayerPosition position) {
//        List<Player> players = playerRepository.findAll();
//        if (pointsPerGame != null){
//            players.removeIf(p -> p.getPointsPerGame() > pointsPerGame);
//        }
//        if(position != null){
//            players.removeIf(p -> p.getPosition() != position);
//        }
//        return players;

        Specification<Player> specification = Specification
                .where(filterEqualsV(Player.class,"position",position))
                .and(lassThan(Player.class, "pointsPerGame", pointsPerGame));

        return playerRepository.findAll(specification);
    }
}
