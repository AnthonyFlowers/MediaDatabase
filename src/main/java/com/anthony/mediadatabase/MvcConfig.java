package com.anthony.mediadatabase;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer{


	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("home");
		registry.addViewController("/tvshows/**").setViewName("tvshows");
		registry.addViewController("/movies/**").setViewName("movies");
		registry.addViewController("/login").setViewName("login");
	}
}
