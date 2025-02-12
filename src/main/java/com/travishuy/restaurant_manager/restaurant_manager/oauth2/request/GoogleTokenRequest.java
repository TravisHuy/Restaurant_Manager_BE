package com.travishuy.restaurant_manager.restaurant_manager.oauth2.request;

import lombok.Data;
/**
 * Represents a request to get the Google access token.
 * This class is used to map the request body to a Java object.
 *
 * @version 0.1
 * @since 04-02-2025
 * @author TravisHuy
 */
@Data
public class GoogleTokenRequest {
    /** The authorization code received from Google */
    private String code;
    /** The client id of the application */
    private String client_id;
    /** The client secret of the application */
    private String client_secret;
    /** The redirect URI of the application */
    private String redirect_uri;
    /** The grant type of the request */
    private String grant_type;
}
