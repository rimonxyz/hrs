package com.moninfotech.service.impl;

import com.moninfotech.domain.User;
import com.moninfotech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by sayemkcn on 4/3/17.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new SecurityUser(this.userService.findByEmail(username));
    }

    class SecurityUser extends User implements UserDetails {

        private User user;

        SecurityUser(User user) {
            this.user = user;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            List<GrantedAuthority> authorityList = new ArrayList<>();
            this.user.getRoles().forEach(role -> {
                authorityList.add(new SimpleGrantedAuthority(role));
            });
            return authorityList;
        }


        @Override
        public String getPassword() {
            return this.user.getPassword();
        }

        @Override
        public String getUsername() {
            return this.user.getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return this.user.isAccountNonExpired();
        }

        @Override
        public boolean isAccountNonLocked() {
            return this.user.isAccountNonLocked();
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return this.user.isCredentialsNonExpired();
        }

        @Override
        public boolean isEnabled() {
            return this.user.isEnabled();
        }


    }
}
