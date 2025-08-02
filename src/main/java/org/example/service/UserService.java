package org.example.service;

import org.example.data.Entity.Role;
import org.example.data.Entity.User;
import org.example.dto.UserRegistrationRequest;
import org.example.dto.UserResponse;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse registerUser(UserRegistrationRequest request){
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new RuntimeException("Username already taken");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(()->new RuntimeException("Default role not found"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encodedPassword);
        user.setRoles(Collections.singleton(userRole));
        User savedUser = userRepository.save(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setUsername(savedUser.getUsername());
        userResponse.setRoles(savedUser.getRoles().stream()
                .map(Role::getName).collect(Collectors.toSet()));
        return userResponse;
    }
}
