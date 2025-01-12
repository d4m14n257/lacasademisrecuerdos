package com.client.service_client.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.User;

import com.client.service_client.model.dto.UserEditDTO;
import com.client.service_client.model.enums.UserStatus;
import com.client.service_client.model.record.UserList;
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

    public List<UserList> getAllUser() {
        List<Object[]> results = userRepository.findAllUsers();
        List<UserList> users = results.stream()
            .map(result -> new UserList(
                (String) result[0],
                (String) result[1],
                (String) result[2],
                UserStatus.valueOf((String) result[3]),
                (String) result[4],
                (String) result[5]))
            .collect(Collectors.toList());

        return users;
    }

    public com.client.service_client.model.User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseGet(() -> userRepository.findByEmail(username).orElse(null));
    }

    public void updateStatus(String id, UserStatus status) {
        userRepository.updateStatus(id, status);
    }

    public void editUser(UserEditDTO entity) {
        userRepository.editUser(entity.getId(), entity.getEmail(), entity.getUsername(), entity.getFirst_name(), entity.getLast_name());
    }

    public String findPasswordById(String id) {
        return userRepository.findPasswordById(id);
    }

    public void updatePassword(String id, String password) {
        userRepository.changePassword(id, password);
    }

    public Optional<com.client.service_client.model.User> findUserById(String id) {
        return userRepository.findById(id);
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
