package com.example.FinalTermProjectJava.service;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.FinalTermProjectJava.Entity.MovieEntity;
import com.example.FinalTermProjectJava.repository.TmDbRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class MovieService {
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	 @Value("${tmdb.api.key}")
	    private String apiKey;
	 
	 @Autowired
	 private RestTemplate restTemplate;
	 
	 @Autowired
	 private TmDbRepository tmDBRepository;
	 
	 ArrayList<MovieEntity> movieList = new ArrayList<>();
	  
	 
	 public boolean checkValidationForExistingDbRecord() {
		 	movieList = (ArrayList<MovieEntity>) tmDBRepository.findAll();
		 	if(movieList.size()>0) {
		 		return true;
		 	}
		return false;
		 
	 }
	 
	 //business logic for hitting 3rd party api and store it in db
	  
	public ArrayList<MovieEntity> getTrendingMovies() throws Exception {
		  
		 
		  	
		  
		  	
		  
	        String url = "https://api.themoviedb.org/3/trending/movie/week?api_key=" + apiKey;
	        String jsonResponse = restTemplate.getForObject(url, String.class);

	        

	        JsonNode root = objectMapper.readTree(jsonResponse);
	        JsonNode results = root.path("results");

	        if (results.isArray()) {
	            for (JsonNode node : results) {
	                MovieEntity movie = new MovieEntity();

	                movie.setTmdbId(node.path("id").asLong());
	                movie.setTitle(node.path("title").asText());
	                movie.setOriginalTitle(node.path("original_title").asText(null));
	                movie.setOverview(node.path("overview").asText(null));
	                movie.setPosterPath(node.path("poster_path").asText(null));
	                movie.setBackdropPath(node.path("backdrop_path").asText(null));
	                movie.setAdult(node.path("adult").asBoolean(false));
	                movie.setOriginalLanguage(node.path("original_language").asText(null));
	                movie.setPopularity(node.path("popularity").asDouble(0.0));
	                movie.setRating(node.path("vote_average").asDouble(0.0));
	                movie.setVoteCount(node.path("vote_count").asInt(0));

	                // Parse release_date string to LocalDate
	                String releaseDateStr = node.path("release_date").asText(null);
	                if (releaseDateStr != null && !releaseDateStr.isEmpty()) {
	                    movie.setReleaseDate(LocalDate.parse(releaseDateStr));
	                }

	                // genre_ids is an array of integers
	                JsonNode genresNode = node.path("genre_ids");
	                if (genresNode.isArray()) {
	                	ArrayList<Integer> genreIds = new ArrayList<>();
	                    for (JsonNode genreIdNode : genresNode) {
	                        genreIds.add(genreIdNode.asInt());
	                    }
	                    movie.setGenreIds(genreIds);
	                }

	                movieList.add(movie);
	            }
	        }

	        movieList = (ArrayList<MovieEntity>) tmDBRepository.saveAll(movieList);
	        return movieList;
	    }



	  
	  //business logic for pagination
	  public Page<MovieEntity> getMoviesPage(Pageable pageable) {
		    return tmDBRepository.findAll(pageable);
		}
}
