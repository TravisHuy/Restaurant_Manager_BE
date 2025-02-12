package com.travishuy.restaurant_manager.restaurant_manager.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
/**
 * Represents a user principal in the system.
 * This class is used to represent the currently logged in user.
 *
 * @version 0.1
 * @since 08-02-2025
 * @author TravisHuy
 */
public class UserPrincipal implements UserDetails {
    /** Unique identifier for the user */
    private String id;
    /** User's email address */
    private String email;
    /** User's password */
    private String password;
    /** User's authorities */
    private Collection<? extends GrantedAuthority> authorities;
    /**
     * Constructs a new UserPrincipal with the given parameters.
     *
     * @param id the unique identifier for the user
     * @param email the email address of the user
     * @param password the password of the user
     * @param authorities the authorities of the user
     */
    public UserPrincipal(String id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }
    /**
     * Creates a new UserPrincipal from the given User object.
     *
     * @param user the user object to create the UserPrincipal from
     * @return the created UserPrincipal
     */
    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
    /**
     * Returns the authorities of the user.
     *
     * @return the authorities of the user
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    /**
     * Returns the unique identifier of the user.
     *
     * @return the unique identifier of the user
     */
    public String getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}