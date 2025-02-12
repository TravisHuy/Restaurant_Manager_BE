package com.travishuy.restaurant_manager.restaurant_manager.oauth2;

import lombok.Data;
/**
 * Represents the user information received from the OAuth2 provider.
 * This class is used to map the response body to a Java object.
 *
 * @version 0.1
 * @since 04-02-2025
 * @author TravisHuy
 */
@Data
public abstract class Oauth2UserInfo {
    /** Unique identifier provided by the OAuth2 provider */
    protected String id;
    /** User's display name or full name */
    protected String name;
    /** User's email address */
    protected String email;
    /** User's profile picture URL */
    protected String imageUrl;
}
