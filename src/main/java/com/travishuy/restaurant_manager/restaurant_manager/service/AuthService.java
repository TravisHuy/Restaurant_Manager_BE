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

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final OAuth2Service oAuth2Service;

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

    public AuthResponse authenticateUser(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new RuntimeException("User không tồn tại"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Mật khẩu không khớp");
        }

        String token = tokenProvider.generateToken(user);
        return new AuthResponse(token,"Xác thực user thành công");
    }

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
