package com.travishuy.restaurant_manager.restaurant_manager.oauth2;

import lombok.Data;

@Data
public abstract class Oauth2UserInfo {
    protected String id;
    protected String name;
    protected String email;
    protected String imageUrl;
}
