package com.travishuy.restaurant_manager.restaurant_manager.oauth2;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GithubUserInfo extends Oauth2UserInfo{
    private String login;
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
