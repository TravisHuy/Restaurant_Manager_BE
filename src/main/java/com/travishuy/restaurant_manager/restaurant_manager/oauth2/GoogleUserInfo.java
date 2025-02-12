package com.travishuy.restaurant_manager.restaurant_manager.oauth2;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents the user information received from Google.
 * This class is used to map the response body to a Java object.
 *
 * @version 0.1
 * @since 04-02-2025
 * @author TravisHuy
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GoogleUserInfo extends Oauth2UserInfo{
    /** The sub of the user */
    private String sub;
    /** The picture of the user */
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
