package com.example.FinalTermProjectJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.FinalTermProjectJava.Entity.MovieEntity;

@Repository
public interface TmDbRepository extends JpaRepository<MovieEntity, Long> {

}
