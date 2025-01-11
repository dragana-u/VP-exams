package mk.ukim.finki.wp.kol2023.g2.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.wp.kol2023.g2.model.Director;
import mk.ukim.finki.wp.kol2023.g2.model.Genre;
import mk.ukim.finki.wp.kol2023.g2.model.Movie;
import mk.ukim.finki.wp.kol2023.g2.model.exceptions.InvalidMovieIdException;
import mk.ukim.finki.wp.kol2023.g2.repository.MovieRepository;
import mk.ukim.finki.wp.kol2023.g2.service.DirectorService;
import mk.ukim.finki.wp.kol2023.g2.service.MovieService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static mk.ukim.finki.wp.kol2023.g2.service.FieldFilterSpecification.filterEqualsV;
import static mk.ukim.finki.wp.kol2023.g2.service.FieldFilterSpecification.lessThan;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final DirectorService directorService;

    @Override
    public List<Movie> listAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Movie findById(Long id) {
        return movieRepository.findById(id).orElseThrow(InvalidMovieIdException::new);
    }

    @Override
    public Movie create(String name, String description, Double rating, Genre genre, Long director) {
        Director directorOfMovie =  directorService.findById(director);
        return movieRepository.save(new Movie(name,description,rating,genre,directorOfMovie));
    }

    @Override
    public Movie update(Long id, String name, String description, Double rating, Genre genre, Long director) {
        Director directorOfMovie =  directorService.findById(director);
        Movie movie = movieRepository.findById(id).orElseThrow(InvalidMovieIdException::new);
        movie.setName(name);
        movie.setDescription(description);
        movie.setDirector(directorOfMovie);
        movie.setRating(rating);
        movie.setGenre(genre);
        return movieRepository.save(movie);
    }

    @Override
    public Movie delete(Long id) {
        Movie movie = this.findById(id);
        movieRepository.delete(movie);
        return movie;
    }

    @Override
    public Movie vote(Long id) {
        Movie movie = this.findById(id);
        movie.setVotes(movie.getVotes() + 1);
        return movieRepository.save(movie);
    }

    @Override
    public List<Movie> listMoviesWithRatingLessThenAndGenre(Double rating, Genre genre) {
        Specification<Movie> specification = Specification
                .where(lessThan(Movie.class, "rating", rating))
                .and(filterEqualsV(Movie.class,"genre",genre));

        return this.movieRepository.findAll(specification);
    }
}
