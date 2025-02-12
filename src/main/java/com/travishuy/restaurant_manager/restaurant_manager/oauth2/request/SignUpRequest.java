package com.travishuy.restaurant_manager.restaurant_manager.oauth2.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
/**
 * Represents a request to sign up for the system.
 * This class is used to map the request body to a Java object.
 *
 * @version 0.1
 * @since 04-02-2025
 * @author TravisHuy
 */
@Data
public class SignUpRequest {
    /** The name of the user */
    @NotBlank(message = "Tên không được để trống")
    private String name;
    /** The email of the user */
    @NotBlank(message = "Email không được để trống")
    @Size(message = "Email không hợp lệ")
    private String email;
    /** The password of the user */
    @NotBlank(message = "Mật khẩu không được để trông")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;
    /** The phone number of the user */
    private String phoneNumber;
    /** The address of the user */
    private String address;
    /** The avatar of the user */
    private String avatar;
}
