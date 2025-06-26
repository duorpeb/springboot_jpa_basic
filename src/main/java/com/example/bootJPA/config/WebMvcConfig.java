package com.example.bootJPA.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
  // 초기화
  String updir = "file:///D:\\Jstl_Servlet_Spring\\_myProject\\_Java\\_fileUpload\\";
  
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/upload/**")
        .addResourceLocations(updir);

  }
}
