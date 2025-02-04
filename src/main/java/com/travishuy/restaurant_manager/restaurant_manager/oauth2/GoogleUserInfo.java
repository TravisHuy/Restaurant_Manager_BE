package com.travishuy.restaurant_manager.restaurant_manager.oauth2;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GoogleUserInfo extends Oauth2UserInfo{
    private String sub;
    private String picture;

    @Override
    public String getId() {
        return sub;
    }

    @Override
    public String getImageUrl() {
        return picture;
    }
}
