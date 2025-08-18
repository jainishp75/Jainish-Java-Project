package com.example.FinalTermProjectJava.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.HiddenHttpMethodFilter;
@Configuration
public class AppConfig {
	  @Bean
	    public RestTemplate restTemplate() {
	        return new RestTemplate();
	    }
	  @Bean
	  public FilterRegistrationBean<HiddenHttpMethodFilter> hiddenHttpMethodFilter(){
	      FilterRegistrationBean<HiddenHttpMethodFilter> filterRegBean = new FilterRegistrationBean<>(new HiddenHttpMethodFilter());
	      filterRegBean.setOrder(1);
	      return filterRegBean;
	  }
}
