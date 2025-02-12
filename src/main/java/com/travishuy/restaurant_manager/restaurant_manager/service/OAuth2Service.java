package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.oauth2.GithubUserInfo;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.GoogleUserInfo;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.Oauth2UserInfo;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.request.GitHubTokenRequest;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.request.GoogleTokenRequest;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.response.GitHubTokenResponse;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.response.GoogleTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
/**
 * Represents the service for the OAuth2.
 * This class is used to get the user info from the OAuth2 provider.
 *
 * @version 0.1
 * @since 04-02-2025
 * @author TravisHuy
 */
@Service
@RequiredArgsConstructor
public class OAuth2Service {
    /** The RestTemplate object */
    private final RestTemplate restTemplate = new RestTemplate();

    /** The URL for the Google token */
    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    /** The URL for the Google user info */
    private static final String GOOGLE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";
    /** The redirect URL for the Google */
    private static final String GOOGLE_REDIRECT_URL = "http://localhost:8080/api/auth/oauth2/callback/google";
    /** The URL for the GitHub token */
    private static final String GITHUB_TOKEN_URL = "https://github.com/login/oauth/access_token";
    /** The URL for the GitHub user info */
    private static final String GITHUB_USER_INFO_URL = "https://api.github.com/user";
    /** The redirect URL for the GitHub */
    private static final String GITHUB_REDIRECT_URL= "http://localhost:8080/api/auth/oauth2/callback/github";

    /** The client ID for the Google */
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    /** The client secret for the Google */
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;
    /** The client ID for the GitHub */
    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String githubClientId;
    /** The client secret for the GitHub */
    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String githubClientSecret;

    /**
     * Gets the user info from the Google.
     *
     * @param code The code to get the user info
     * @return The user info from the Google
     */
    public Oauth2UserInfo getGoogleUserInfo(String code){
        // get access token
        GoogleTokenResponse tokenResponse = restTemplate.postForObject(GOOGLE_TOKEN_URL,createGoogleTokenRequest(code),GoogleTokenResponse.class);

        //get user info
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenResponse.getAccess_token());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GoogleUserInfo> response = restTemplate.exchange(GOOGLE_USER_INFO_URL, HttpMethod.GET,entity,GoogleUserInfo.class);
        return response.getBody();

    }
    /**
     * Creates the Google token request.
     *
     * @param code The code to create the Google token request
     * @return The Google token request
     */
    private HttpEntity<GoogleTokenRequest> createGoogleTokenRequest(String code){
        GoogleTokenRequest request = new GoogleTokenRequest();
        request.setCode(code);
        request.setClient_id(googleClientId);
        request.setClient_secret(googleClientSecret);
        request.setGrant_type("authorization_code");
        request.setRedirect_uri(GOOGLE_REDIRECT_URL);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        return new HttpEntity<>(request,headers);
    }
    /**
     * Gets the user info from the GitHub.
     *
     * @param code The code to get the user info
     * @return The user info from the GitHub
     */
    public Oauth2UserInfo getGithubUserInfo(String code){
        // get access token
        GitHubTokenResponse tokenResponse = restTemplate.postForObject(GITHUB_TOKEN_URL,createGithubTokenRequest(code), GitHubTokenResponse.class);

        //get user info
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenResponse.getAccess_token());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GithubUserInfo> response = restTemplate.exchange(GITHUB_USER_INFO_URL, HttpMethod.GET,entity, GithubUserInfo.class);
        return response.getBody();

    }
    /**
     * Creates the GitHub token request.
     *
     * @param code The code to create the GitHub token request
     * @return The GitHub token request
     */
    private HttpEntity<GitHubTokenRequest> createGithubTokenRequest(String code){
        GitHubTokenRequest request = new GitHubTokenRequest();
        request.setCode(code);
        request.setClient_id(githubClientId);
        request.setClient_secret(githubClientSecret);
        request.setRedirect_uri(GITHUB_REDIRECT_URL);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        return new HttpEntity<>(request,headers);
    }
}
