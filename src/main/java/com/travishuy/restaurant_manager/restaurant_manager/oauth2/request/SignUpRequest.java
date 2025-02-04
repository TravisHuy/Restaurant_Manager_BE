package com.travishuy.restaurant_manager.restaurant_manager.oauth2.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {
    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotBlank(message = "Email không được để trống")
    @Size(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trông")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    private String phoneNumber;
    private String address;
    private String avatar;
}
