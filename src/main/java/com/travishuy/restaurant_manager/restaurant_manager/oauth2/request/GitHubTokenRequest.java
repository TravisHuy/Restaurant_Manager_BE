package com.travishuy.restaurant_manager.restaurant_manager.oauth2.request;

import lombok.Data;

@Data
public class GitHubTokenRequest {
    private String code;
    private String client_id;
    private String client_secret;
    private String redirect_uri;
}
