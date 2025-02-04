package com.travishuy.restaurant_manager.restaurant_manager.oauth2.response;

import lombok.Data;

@Data
public class GoogleTokenResponse {
    private String access_token;
    private String id_token;
    private String token_type;
    private String expires_in;
}
