package mk.ukim.finki.wp.kol2021.restaurant.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface JpaSpecificationRepository<T, ID> extends JpaRepository<T, ID> {
    List<T> findAll(Specification<T> filter);
}
