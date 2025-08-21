package com.example.FinalTermProjectJava.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.example.FinalTermProjectJava.Entity.MovieEntity;
import com.example.FinalTermProjectJava.cache.CacheInspector;
import com.example.FinalTermProjectJava.repository.TmDbRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MovieService {
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	 @Value("${tmdb.api.key}")
	    private String apiKey;
	 
	 @Autowired
	 private RestTemplate restTemplate;
	 
	 @Autowired
	 private TmDbRepository tmDBRepository;
	 
	 @Autowired
	 private CacheInspector  cacheInspector;  
	 
	 ArrayList<MovieEntity> movieList = new ArrayList<>();
	 ArrayList<MovieEntity> existingMovieList = new ArrayList<>();
	 
	 
	  
	 
	 public boolean checkValidationForExistingDbRecord() {
		 	movieList = (ArrayList<MovieEntity>) tmDBRepository.findAll();
		 	if(movieList.size()>0) {
		 		return true;
		 	}
		return false;
		 
	 }
	 
	 //business logic for hitting 3rd party api and store it in db
	 
	 @Scheduled(fixedRate = 120000)
	public ArrayList<MovieEntity> getTrendingMovies() throws Exception {

	        String url = "https://api.themoviedb.org/3/trending/movie/week?api_key=" + apiKey;
	        String jsonResponse = restTemplate.getForObject(url, String.class);
	        
	        System.out.println(jsonResponse);

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
	        
	        
	        //feching all existing movie
	        existingMovieList = (ArrayList<MovieEntity>) tmDBRepository.findAll();
	        
	        //fetching existing of API TmDb Id of existing movie list
	        Set<Long> existingIds = existingMovieList.stream()
	                .map(MovieEntity::getTmdbId)  // or imdbId, or any unique identifier
	                .collect(Collectors.toSet());
	        
	        

	     // Keep only new movies from API
	        ArrayList<MovieEntity> newMovies = movieList.stream()
	        	    .filter(movie -> !existingIds.contains(movie.getTmdbId()))
	        	    .collect(Collectors.toCollection(ArrayList::new));
	        
	        log.debug("found new movies =>"+newMovies.size());
	     // Save new ones
	        if (!newMovies.isEmpty()) {
	            tmDBRepository.saveAll(newMovies);
	            log.info("number of new record inserted : "+newMovies.size());
	        }
	        
	        return movieList;
	    }



	  
	  //business logic for pagination
	 @Cacheable(value = "moviesPage", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
	  public Page<MovieEntity> getMoviesPage(Pageable pageable) {
		  System.out.println(">>> Fetching from DB...");
		       
		    return tmDBRepository.findAllSoftDeleted(pageable);
		}

	 //business logic for soft delete
	   @CacheEvict(value = "moviesPage", allEntries = true)
	  public boolean deleteMovieThroughId(Long id) {
		  int numberOfSoftDeletedRecords;
		  numberOfSoftDeletedRecords = tmDBRepository.movieSoftDelete(id);
		return numberOfSoftDeletedRecords > 0;
	  }
	  
	   //runs every 2 minutes
	   @CacheEvict(value = "moviesPage", allEntries = true)
	    @Scheduled(fixedRate = 120000) // 120000ms = 2 minutes
	    @Transactional
	    public void removeSoftDeletedMovies() {
	    	tmDBRepository.deleteByIsDeleteTrue();
	        log.info("Soft-deleted movies permanently removed at " + java.time.LocalDateTime.now());
	    }

	   //business logic for set movie favourite
	   @CacheEvict(value="moviesPage" , allEntries=true)
	   public Boolean setFavouriteMovieThroughId(Long id) {
		   int numberOfSetMovieFavourite = tmDBRepository.setMovieFavourite(id);
		   return numberOfSetMovieFavourite > 0;
		   
		
		   // TODO Auto-generated method stub
		   
	   }
		public void debugCache(String string) {
			// TODO Auto-generated method stub
			cacheInspector.printCache(string);//printing existing cache data
			
		}
		@CacheEvict(value="moviesPage" , allEntries=true)
		public Boolean setUnFavouriteMovieThroughId(Long id) {
			int numberOfSetMovieFavourite = tmDBRepository.setUnMovieFavourite(id);
			   return numberOfSetMovieFavourite > 0;
			
		}

}
