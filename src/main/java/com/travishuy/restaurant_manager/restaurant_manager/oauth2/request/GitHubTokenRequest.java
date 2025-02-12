package com.travishuy.restaurant_manager.restaurant_manager.oauth2.request;

import lombok.Data;
/**
 * Represents a request to get the GitHub access token.
 * This class is used to map the request body to a Java object.
 *
 * @version 0.1
 * @since 04-02-2025
 * @author TravisHuy
 */
@Data
public class GitHubTokenRequest {
    /** The authorization code received from GitHub */
    private String code;
    /** The client id of the application */
    private String client_id;
    /** The client secret of the application */
    private String client_secret;
    /** The redirect URI of the application */
    private String redirect_uri;
}
