package com.example.FinalTermProjectJava.Entity;

import java.time.LocalDate;
import java.util.List;



import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movies")

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TMDb movie ID (unique per movie)
    @Column(nullable = false, unique = true)
    private Long tmdbId;

    @Column(nullable = false)
    private String title;

    @Column(name = "original_title")
    private String originalTitle;

    @Column(length = 2000)
    private String overview;

    private String posterPath;
    
    private String backdropPath;

    private LocalDate releaseDate;

    private Double rating; 

    private Boolean adult;

    private String originalLanguage;

    private Double popularity;

    private Integer voteCount;
    
    private Boolean isDelete = false ;

    @ElementCollection
    @CollectionTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "genre_id")
    private List<Integer> genreIds;

}
