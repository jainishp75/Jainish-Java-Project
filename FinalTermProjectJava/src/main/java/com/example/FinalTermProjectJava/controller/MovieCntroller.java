package com.example.FinalTermProjectJava.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.FinalTermProjectJava.Entity.MovieEntity;
import com.example.FinalTermProjectJava.service.MovieService;
import com.example.FinalTermProjectJava.utils.Response;

@Controller
public class MovieCntroller {
	
	@Autowired
	private MovieService movieService;
	
	ArrayList<MovieEntity> trendingMovies = new ArrayList<MovieEntity>();
	
	MovieEntity favouriteMovie ;
	

	
	
	@GetMapping("/fetchALL")
	public String fetchAllMoviesFromDb(
	        Model model,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(required = false) Integer size) {

	    if (size == null) size = 5;  // default value if null

	    Pageable pageable = PageRequest.of(page, size);
	    Page<MovieEntity> moviePage = movieService.getMoviesPage(pageable);

	    model.addAttribute("movies", moviePage.getContent());
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", moviePage.getTotalPages());
	    model.addAttribute("size", size);
	    
	    movieService.debugCache("moviesPage");
	    
	    return "movies";
	}
	
	@PutMapping("/deleteMovie/{id}")
	public String deleteMovie(@PathVariable Long id) {
		Boolean deleteFlag;
		deleteFlag = movieService. deleteMovieThroughId(id);
	   
	    return "redirect:/fetchALL"; 
	}
	
	@PutMapping("/favouriteMovie/{id}")
	@ResponseBody
	public Response favouriteMovie(@PathVariable Long id) {
		boolean resultFlag;
		resultFlag = movieService.setFavouriteMovieThroughId(id);  
		if(resultFlag) {
			return Response.getResponse(200, "Movie marked as favourite....", null);
		}
		return Response.getResponse(500, "Failed to update favourite status....", null);
	}
	
	@PutMapping("/unFavouriteMovie/{id}")
	@ResponseBody
	public Response unFavouriteMovie(@PathVariable Long id) {
		boolean resultFlag;
		resultFlag = movieService.setUnFavouriteMovieThroughId(id);  
		if(resultFlag) {
			return Response.getResponse(200, "Movie unmarked as favourite....", null);
		}
		return Response.getResponse(500, "Failed to update favourite status....", null);
	}



}
