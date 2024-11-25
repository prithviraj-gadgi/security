package com.security.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JWTService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Creating a dummy set of authorities, need to add roles User model to have authorities
        Set<String> roles = new HashSet<>();
        roles.add("ADMIN");
        roles.add("USER");

        return new User(username, "Gadgi@2000", roles.stream().map(SimpleGrantedAuthority::new).toList());
    }
}
