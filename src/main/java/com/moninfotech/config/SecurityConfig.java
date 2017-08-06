package com.moninfotech.config;

import com.moninfotech.commons.Constants;
import com.moninfotech.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by sayemkcn on 4/3/17.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/fonts/**", "/adminlte/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/bookings/cart/**", "/hotels/**","/ships/**", "/rooms/**", "/rest/**", "/", "/search/**", "/login", "/users/exists/*", "/logout", "/register", "/fonts/**", "/js/**", "/css/**", "/images/**").permitAll()
                .antMatchers("/admin/**").hasRole(Constants.Roles.AUTHORITY_ADMIN)
                .antMatchers("/hotel/**").hasAnyRole(Constants.Roles.AUTHORITY_HOTEL_ADMIN, Constants.Roles.AUTHORITY_ADMIN)
                .antMatchers("/bookings/**").hasAnyRole(Constants.Roles.RAUTHORITY_USER, Constants.Roles.AUTHORITY_HOTEL_ADMIN, Constants.Roles.AUTHORITY_ADMIN)
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .failureUrl("/login?error")
                .defaultSuccessUrl("/dashboard")
                .permitAll()
                .and()
                .rememberMe()
                .key("absym3h4uhd");
        http
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
//                .inMemoryAuthentication()
//                .withUser("admin").password("pass").roles("USER","HOTEL","ADMIN");
                .userDetailsService(this.userDetailsService);
//                .passwordEncoder(new ShaPasswordEncoder(256));
    }
}
