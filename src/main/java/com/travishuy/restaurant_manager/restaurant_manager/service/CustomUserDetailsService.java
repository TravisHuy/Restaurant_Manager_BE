package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.model.User;
import com.travishuy.restaurant_manager.restaurant_manager.model.UserPrincipal;
import com.travishuy.restaurant_manager.restaurant_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Represents the service for the custom user details.
 * This class is used to load the user by username.
 *
 * @version 0.1
 * @since 08-02-2025
 * @author TravisHuy
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    /** The repository for the user */
    @Autowired
    private UserRepository userRepository;
    /**
     * Loads the user by username.
     *
     * @param email The email of the user
     * @return The user details
     * @throws UsernameNotFoundException If the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return UserPrincipal.create(user);
    }
    /**
     * Loads the user by id.
     *
     * @param id The id of the user
     * @return The user details
     * @throws UsernameNotFoundException If the user is not found
     */
    public UserDetails loadUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        return UserPrincipal.create(user);
    }
}
