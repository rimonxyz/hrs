package com.moninfotech.config.security;

import com.moninfotech.commons.Constants;
import com.moninfotech.commons.utils.PasswordUtil;
import com.moninfotech.domain.User;
import com.moninfotech.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by sayemkcn on 4/3/17.
 */
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
                .antMatchers(
                        "/oauth/token",
                        "/api/v1/**",
                        "/bookings/cart/**",
                        "/hotels/**",
                        "/ships/**",
                        "/packages/**",
                        "/offers/**",
                        "/feedback/**",
                        "/user/validation/**",
                        "/rooms/**",
                        "/rest/**",
                        "/",
                        "/search/**",
                        "/login",
                        "/users/exists/*",
                        "/logout",
                        "/register",
                        "/resetPassword/**",
                        "/fonts/**",
                        "/js/**",
                        "/css/**",
                        "/images/**",
                        "/newsletter/subscribe",
                        "/bookings/checkout",
                        "/bookings/tempRegister*",
                        "/test"
                )
                .permitAll()
                .antMatchers("/admin/**").hasRole(Constants.Roles.AUTHORITY_ADMIN)
                .antMatchers("/hotel/**").hasAnyRole(Constants.Roles.AUTHORITY_HOTEL_ADMIN, Constants.Roles.AUTHORITY_ADMIN)
                .antMatchers("/bookings/**").hasAnyRole(Constants.Roles.AUTHORITY_USER, Constants.Roles.AUTHORITY_AGENT, Constants.Roles.AUTHORITY_HOTEL_ADMIN, Constants.Roles.AUTHORITY_ADMIN)
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


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
////                .inMemoryAuthentication()
////                .withUser("admin").password("pass").roles("USER","HOTEL","ADMIN");
//                .userDetailsService(this.userDetailsService)
//                .passwordEncoder(PasswordUtil.getBCryptPasswordEncoder());
//    }


    public static boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                //when Anonymous Authentication is enabled
                !(SecurityContextHolder.getContext().getAuthentication()
                        instanceof AnonymousAuthenticationToken);
    }

    public static void setAuthentication(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
