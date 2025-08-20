package com.example.FinalTermProjectJava.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Query("UPDATE MovieEntity m SET m.isDelete = true WHERE m.id = :id")
	int movieSoftDelete(@Param("id") Long id);


    @Query("SELECT m FROM MovieEntity m WHERE m.isDelete = false ORDER BY m.id ASC")
	Page<MovieEntity> findAllSoftDeleted(Pageable pageable);


    @Modifying
    @Query("DELETE FROM MovieEntity m WHERE m.isDelete = true")
	void deleteByIsDeleteTrue();

    @Modifying
    @Transactional
    @Query("UPDATE MovieEntity m SET m.isFavourite = true WHERE m.id = :id")
	int setMovieFavourite(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE MovieEntity m SET m.isFavourite = false WHERE m.isFavourite = true AND  m.id = :id")
	int setUnMovieFavourite(Long id);
    
    

	

}
