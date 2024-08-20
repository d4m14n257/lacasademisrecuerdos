package com.client.service_client.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import com.client.service_client.repository.UserRepository;

@Service
public class UserService implements UserDetailsService{

    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean existsUser(String username, String email) {
        Boolean check = userRepository.existsUser(username, email);
        return check;
    }

    public com.client.service_client.model.User findUserByUsername(String username) {
        Optional<com.client.service_client.model.User> user = userRepository.findByUsername(username);

        if(user.isPresent())
            return user.get();

        return null;
    }

    @Transactional
    public void save (com.client.service_client.model.User user) {
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.client.service_client.model.User user = userRepository.findByUsername(username)
            .orElseGet(() -> userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found")));

        return new User(user.getUsername(), user.getPassword(), Collections.emptyList());
    }
}
