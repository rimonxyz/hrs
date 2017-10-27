package com.moninfotech;

import com.moninfotech.commons.utils.PasswordUtil;
import com.moninfotech.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootApplication
@EnableScheduling
public class HrsApplication {

    private final CustomUserDetailsService customUserDetailsService;

    public static void main(String[] args) {
        SpringApplication.run(HrsApplication.class, args);
    }

    @Autowired
    public HrsApplication(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Autowired
    public void authenticationManager(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(customUserDetailsService).passwordEncoder(PasswordUtil.getBCryptPasswordEncoder());
    }

}
