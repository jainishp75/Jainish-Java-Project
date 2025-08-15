package com.example.FinalTermProjectJava.utils;

import java.util.ArrayList;

import com.example.FinalTermProjectJava.Entity.MovieEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public  class Response {
	
	private int statusCode;
	
	private  String message;
	
	private  ArrayList<MovieEntity> movieEntity;
	

	public static Response getResponse(int statusCode, String message, ArrayList<MovieEntity> movieEntity) {
		 return new Response(statusCode, message, movieEntity);
	}
}
