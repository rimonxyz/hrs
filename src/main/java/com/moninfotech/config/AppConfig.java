package com.moninfotech.config;

import com.moninfotech.interceptors.ActivityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class AppConfig extends WebMvcConfigurerAdapter{

    private final ActivityInterceptor activityInterceptor;

    @Autowired
    public AppConfig(ActivityInterceptor activityInterceptor) {
        this.activityInterceptor = activityInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(activityInterceptor);
    }

}
