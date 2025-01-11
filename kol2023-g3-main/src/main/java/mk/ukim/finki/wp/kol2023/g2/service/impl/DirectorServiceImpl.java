package mk.ukim.finki.wp.kol2023.g2.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.wp.kol2023.g2.model.Director;
import mk.ukim.finki.wp.kol2023.g2.model.exceptions.InvalidDirectorIdException;
import mk.ukim.finki.wp.kol2023.g2.repository.DirectorRepository;
import mk.ukim.finki.wp.kol2023.g2.service.DirectorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {
    private final DirectorRepository directorRepository;

    @Override
    public Director findById(Long id) {
        return directorRepository.findById(id).orElseThrow(InvalidDirectorIdException::new);
    }

    @Override
    public List<Director> listAll() {
        return directorRepository.findAll();
    }

    @Override
    public Director create(String name) {
        return directorRepository.save(new Director(name));
    }
}
