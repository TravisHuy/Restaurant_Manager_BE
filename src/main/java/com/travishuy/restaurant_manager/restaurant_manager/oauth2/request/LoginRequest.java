package com.travishuy.restaurant_manager.restaurant_manager.oauth2.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
/**
 * Represents a request to login to the system.
 * This class is used to map the request body to a Java object.
 *
 * @version 0.1
 * @since 04-02-2025
 * @author TravisHuy
 */
@Data
public class LoginRequest {

    /** The email of the user */
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    /** The password of the user */
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}
