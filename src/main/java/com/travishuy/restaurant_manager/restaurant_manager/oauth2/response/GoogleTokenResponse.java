package com.travishuy.restaurant_manager.restaurant_manager.oauth2.response;

import lombok.Data;
/**
 * Represents a response to the Google access token request.
 * This class is used to map the response body to a Java object.
 *
 * @version 0.1
 * @since 04-02-2025
 * @author TravisHuy
 */
@Data
public class GoogleTokenResponse {
    /** The access token of the user */
    private String access_token;
    /** The refresh token of the user */
    private String id_token;
    /** The token type of the user */
    private String token_type;
    /** The scope of the user */
    private String expires_in;
}
