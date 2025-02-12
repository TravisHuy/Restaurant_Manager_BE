package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.jwt.JwtTokenProvider;
import com.travishuy.restaurant_manager.restaurant_manager.model.AuthProvider;
import com.travishuy.restaurant_manager.restaurant_manager.model.User;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.Oauth2UserInfo;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.request.LoginRequest;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.request.SignUpRequest;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.response.AuthResponse;
import com.travishuy.restaurant_manager.restaurant_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Represents the service for the authentication.
 * This class is used to authenticate the user.
 *
 * @version 0.1
 * @since 04-02-2025
 * @author TravisHuy
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    /** The repository for the User model */
    private final UserRepository userRepository;
    /** The encoder for the password */
    private final PasswordEncoder passwordEncoder;
    /** The provider for the JWT token */
    private final JwtTokenProvider tokenProvider;
    /** The manager for the authentication */
    private final AuthenticationManager authenticationManager;
    /** The service for the OAuth2 */
    private final OAuth2Service oAuth2Service;

    /**
     * Registers a new user.
     *
     * @param request The request to register the user
     * @return The response to the authentication request
     */
    public AuthResponse registerUser(SignUpRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email đã tồn tại");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setRole("USER");
        user.setProvider(AuthProvider.LOCAL);

        User savedUser = userRepository.save(user);
        String token = tokenProvider.generateToken(savedUser);

        return new AuthResponse(token, "Đăng ký tài khoản thành công");
    }
    /**
     * Authenticates a user.
     *
     * @param request The request to authenticate the user
     * @return The response to the authentication request
     */
    public AuthResponse authenticateUser(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new RuntimeException("User không tồn tại"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Mật khẩu không khớp");
        }

        String token = tokenProvider.generateToken(user);
        return new AuthResponse(token,"Xác thực user thành công");
    }
    /**
     * Processes the OAuth2 login.
     *
     * @param provider The provider of the OAuth2
     * @param code The code of the OAuth2
     * @return The response to the authentication request
     */
    public AuthResponse processOAuth2Login(String provider, String code){
        Oauth2UserInfo userInfo;

        String providerStr = provider.toUpperCase();

        if("GOOGLE".equals(providerStr)){
            userInfo = oAuth2Service.getGoogleUserInfo(code);
        }
        else if ("GITHUB".equals(providerStr)){
            userInfo = oAuth2Service.getGithubUserInfo(code);
        }
        else{
            throw new RuntimeException("Không hỗ trợ đăng nhập");
        }

        User user = userRepository.findByEmail(userInfo.getEmail()).orElseGet(()-> createOAuth2User(userInfo,AuthProvider.valueOf(provider)));

        String token = tokenProvider.generateToken(user);
        return new AuthResponse(token,"Đăng nhập thành công");
    }
    /**
     * Creates a new user with the OAuth2 information.
     *
     * @param userInfo The information of the user
     * @param provider The provider of the OAuth2
     * @return The user created
     */
    private User createOAuth2User(Oauth2UserInfo userInfo, AuthProvider provider) {
        User user = new User();
        user.setName(userInfo.getName());
        user.setEmail(userInfo.getEmail());
        user.setAvatar(userInfo.getImageUrl());
        user.setProvider(provider);
        user.setProviderId(userInfo.getId());
        user.setRole("USER");
        return userRepository.save(user);
    }
}
