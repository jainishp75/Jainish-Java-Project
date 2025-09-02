package com.example.FinalTermProjectJava.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.FinalTermProjectJava.Entity.MovieEntity;

import jakarta.transaction.Transactional;

@Repository
public interface TmDbRepository extends JpaRepository<MovieEntity, Long> {




   



    @Modifying
    @Transactional
    @Query("UPDATE MovieEntity m SET m.isFavourite = true WHERE m.id = :id")
	int setMovieFavourite(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE MovieEntity m SET m.isFavourite = false WHERE m.isFavourite = true AND  m.id = :id")
	int setUnMovieFavourite(@Param("id") Long id);
    
    

	

}
