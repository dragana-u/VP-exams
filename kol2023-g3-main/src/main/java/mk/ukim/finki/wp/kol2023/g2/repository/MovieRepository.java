package mk.ukim.finki.wp.kol2023.g2.repository;

import mk.ukim.finki.wp.kol2023.g2.model.Movie;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaSpecificationRepository<Movie, Long> {
}
