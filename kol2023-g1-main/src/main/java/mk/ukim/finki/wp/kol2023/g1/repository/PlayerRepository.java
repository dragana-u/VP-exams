package mk.ukim.finki.wp.kol2023.g1.repository;

import mk.ukim.finki.wp.kol2023.g1.model.Player;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaSpecificationRepository<Player, Long> {
}
