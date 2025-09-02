package com.example.FinalTermProjectJava.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "archived_movies")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArchivedMovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tmdb_id", unique = true, nullable = false)
    private Long tmdbId;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String originalTitle;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String overview;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String posterPath;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String backdropPath;

    private LocalDate releaseDate;

    private Double rating;

    private Boolean adult;

    @Column(length = 10)
    private String originalLanguage;

    private Double popularity;

    private Integer voteCount;

    private Boolean isFavourite;

    private LocalDate archivedAt = LocalDate.now();
}
