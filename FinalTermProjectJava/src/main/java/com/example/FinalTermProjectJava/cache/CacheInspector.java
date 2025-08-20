package com.example.FinalTermProjectJava.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CacheInspector {
	
	@Autowired
    private final CacheManager cacheManager;
	
	public void printCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            System.out.println("Cache '" + cacheName + "' not found.");
            return;
        }

        System.out.println("Cache: " + cacheName + " -> " + cache.getNativeCache());
    }
	

}
