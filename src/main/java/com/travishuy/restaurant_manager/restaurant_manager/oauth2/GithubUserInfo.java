package com.travishuy.restaurant_manager.restaurant_manager.oauth2;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * Represents the user information received from GitHub.
 * This class is used to map the response body to a Java object.
 *
 * @version 0.1
 * @since 04-02-2025
 * @author TravisHuy
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GithubUserInfo extends Oauth2UserInfo{
    /** The login of the user */
    private String login;
    /** The avatar URL of the user */
    private String avatar_url;

    @Override
    public String getId() {
        return login;
    }

    @Override
    public String getImageUrl() {
        return avatar_url;
    }
}
