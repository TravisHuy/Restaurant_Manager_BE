package com.travishuy.restaurant_manager.restaurant_manager.oauth2.response;

import lombok.Data;

@Data
public class GitHubTokenResponse {
    private String access_token;
    private String token_type;
    private String scope;
}
