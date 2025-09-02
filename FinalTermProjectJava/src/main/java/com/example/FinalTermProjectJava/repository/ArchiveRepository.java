package com.example.FinalTermProjectJava.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.FinalTermProjectJava.Entity.ArchivedMovieEntity;
import com.example.FinalTermProjectJava.Entity.MovieEntity;

@Repository
public interface ArchiveRepository extends JpaRepository<ArchivedMovieEntity, Long> {

    // Optional custom method
    boolean existsByTmdbId(Long tmdbId);

	Optional<MovieEntity> save(MovieEntity movieEntity);

	
}
