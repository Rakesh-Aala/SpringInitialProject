package org.example.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserProfileController {

    @GetMapping("/profile")
    public UserProfile getProfile(Authentication authentication) {
        String username = authentication.getName();
        Set<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return new UserProfile(username, roles);
    }

    // Inner class to represent the response
    public static class UserProfile {
        private String username;
        private Set<String> roles;

        public UserProfile(String username, Set<String> roles) {
            this.username = username;
            this.roles = roles;
        }

        public String getUsername() { return username; }
        public Set<String> getRoles() { return roles; }
    }
}

