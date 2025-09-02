package com.example.FinalTermProjectJava.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import org.springframework.web.client.RestTemplate;

import com.example.FinalTermProjectJava.Entity.ArchivedMovieEntity;
import com.example.FinalTermProjectJava.Entity.MovieEntity;
import com.example.FinalTermProjectJava.cache.CacheInspector;
import com.example.FinalTermProjectJava.repository.ArchiveRepository;
import com.example.FinalTermProjectJava.repository.TmDbRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
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
	 private ArchiveRepository archievedMovieRepository;
	 
	 @Autowired
	 private CacheInspector  cacheInspector;  
	 
	 ArrayList<MovieEntity> movieList = new ArrayList<>();
	 ArrayList<MovieEntity> existingMovieList = new ArrayList<>();
	 Optional<MovieEntity> movie = null ;
	 
	  
	 
	 public boolean checkValidationForExistingDbRecord() {
		 	movieList = (ArrayList<MovieEntity>) tmDBRepository.findAll();
		 	if(movieList.size()>0) {
		 		return true;
		 	}
		return false;
		 
	 }
	 
	 //business logic for hitting 3rd party api and store it in db
	 
	  @Scheduled(fixedRate = 120000) // runs every 2 minutes
	  @Transactional
	  public void syncTrendingMovies() throws Exception {
	      String url = "https://api.themoviedb.org/3/trending/movie/week?api_key=" + apiKey;
	      String jsonResponse = restTemplate.getForObject(url, String.class);

	      JsonNode root = objectMapper.readTree(jsonResponse);
	      JsonNode results = root.path("results");

	      if (!results.isArray() || results.isEmpty()) return;

	      List<MovieEntity> apiMovies = new ArrayList<>();
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

	          String releaseDateStr = node.path("release_date").asText(null);
	          if (releaseDateStr != null && !releaseDateStr.isEmpty()) {
	              movie.setReleaseDate(LocalDate.parse(releaseDateStr));
	          }

	          JsonNode genresNode = node.path("genre_ids");
	          if (genresNode.isArray()) {
	              List<Integer> genreIds = new ArrayList<>();
	              for (JsonNode genreIdNode : genresNode) {
	                  genreIds.add(genreIdNode.asInt());
	              }
	              movie.setGenreIds(genreIds);
	          }

	          apiMovies.add(movie);
	      }

	      // Fetch all existing movies
	      Map<Long, MovieEntity> existingMovies = tmDBRepository.findAll()
	              .stream()
	              .collect(Collectors.toMap(MovieEntity::getTmdbId, m -> m));

	      // Fetch all archived movies
	      Set<Long> archivedIds = archievedMovieRepository.findAll()
	              .stream()
	              .map(ArchivedMovieEntity::getTmdbId)
	              .collect(Collectors.toSet());

	      List<MovieEntity> newMovies = new ArrayList<>();
	      List<MovieEntity> updatedMovies = new ArrayList<>();

	      for (MovieEntity apiMovie : apiMovies) {
	          // Skip if the movie exists in archive
	          if (archivedIds.contains(apiMovie.getTmdbId())) continue;

	          MovieEntity existing = existingMovies.get(apiMovie.getTmdbId());

	          if (existing == null) {
	              // New movie
	              newMovies.add(apiMovie);
	          } else {
	              // Update existing movie
	              existing.setTitle(apiMovie.getTitle());
	              existing.setOriginalTitle(apiMovie.getOriginalTitle());
	              existing.setOverview(apiMovie.getOverview());
	              existing.setPosterPath(apiMovie.getPosterPath());
	              existing.setBackdropPath(apiMovie.getBackdropPath());
	              existing.setAdult(apiMovie.getAdult());
	              existing.setOriginalLanguage(apiMovie.getOriginalLanguage());
	              existing.setPopularity(apiMovie.getPopularity());
	              existing.setRating(apiMovie.getRating());
	              existing.setVoteCount(apiMovie.getVoteCount());
	              existing.setReleaseDate(apiMovie.getReleaseDate());
	              existing.setGenreIds(apiMovie.getGenreIds());
	              updatedMovies.add(existing);
	          }
	      }

	      if (!newMovies.isEmpty()) {
	          tmDBRepository.saveAll(newMovies);
	          log.info("Inserted {} new movies", newMovies.size());
	      }

	      if (!updatedMovies.isEmpty()) {
	          tmDBRepository.saveAll(updatedMovies);
	          log.info("Updated {} existing movies", updatedMovies.size());
	      }
	  }


	  
	  //business logic for pagination
	  @Cacheable(value = "moviesPage", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
	  public Page<MovieEntity> getMoviesPage(Pageable pageable) {
	      System.out.println(">>> Fetching from DB...");
	      return tmDBRepository.findAll(pageable);
	  }

	  @CacheEvict(value = "moviesPage", allEntries = true)
	  public Optional<MovieEntity> deleteMovieThroughId(Long id) {
	      Optional<MovieEntity> movieOpt = tmDBRepository.findById(id);

	      if (movieOpt.isPresent()) {
	          MovieEntity movieEntity = movieOpt.get();
	          System.out.println("Movie  Entity is=>"+movieEntity);


	          ArchivedMovieEntity archived = new ArchivedMovieEntity();
	          archived.setTmdbId(movieEntity.getTmdbId());
	          archived.setTitle(movieEntity.getTitle());
	          archived.setOriginalTitle(movieEntity.getOriginalTitle());
	          archived.setOverview(movieEntity.getOverview());
	          archived.setPosterPath(movieEntity.getPosterPath());
	          archived.setBackdropPath(movieEntity.getBackdropPath());
	          archived.setReleaseDate(movieEntity.getReleaseDate());
	          archived.setRating(movieEntity.getRating());
	          archived.setAdult(movieEntity.getAdult());
	          archived.setOriginalLanguage(movieEntity.getOriginalLanguage());
	          archived.setPopularity(movieEntity.getPopularity());
	          archived.setVoteCount(movieEntity.getVoteCount());
	          archived.setIsFavourite(movieEntity.getIsFavourite());
	          archived.setArchivedAt(LocalDate.now());

	          System.out.println("ArchivedMovieEntity  Entity is=>"+archived);
	          if (movieEntity.getTmdbId() != null) {
	        	// Save to archive
		          archievedMovieRepository.save(archived);

	        	}
	          
	          tmDBRepository.deleteById(id);
	          

	          return Optional.of(movieEntity);
	      }

	      return Optional.empty();
	  }
	  


	   //business logic for set movie favourite
	   @CacheEvict(value="moviesPage" , allEntries=true)
	   public Boolean setFavouriteMovieThroughId(Long id) {
		   int numberOfSetMovieFavourite = tmDBRepository.setMovieFavourite(id);
		   return numberOfSetMovieFavourite > 0;		   
	   }
		public void debugCache(String string) {
			cacheInspector.printCache(string);//printing existing cache data
		}
		@CacheEvict(value="moviesPage" , allEntries=true)
		public Boolean setUnFavouriteMovieThroughId(Long id) {
			int numberOfSetMovieFavourite = tmDBRepository.setUnMovieFavourite(id);
			   return numberOfSetMovieFavourite > 0;
			
		}
		
		//cron job for delete movie of archived table
		 @Scheduled(fixedRate = 240000) // runs every 4 minutes
		  @Transactional
		  public void deleteAllMoviesFromArchive() {
			 long count = archievedMovieRepository.count();
			 archievedMovieRepository.deleteAll();
			 log.warn(count + " records deleted from archived");
		 }
		 

}
