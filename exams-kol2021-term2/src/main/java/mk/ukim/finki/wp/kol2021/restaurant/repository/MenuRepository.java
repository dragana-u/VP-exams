package mk.ukim.finki.wp.kol2021.restaurant.repository;

import mk.ukim.finki.wp.kol2021.restaurant.model.Menu;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaSpecificationRepository<Menu, Long> {
}
