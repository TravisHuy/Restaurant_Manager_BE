package com.travishuy.restaurant_manager.restaurant_manager.service;

import com.travishuy.restaurant_manager.restaurant_manager.jwt.JwtTokenProvider;
import com.travishuy.restaurant_manager.restaurant_manager.model.AuthProvider;
import com.travishuy.restaurant_manager.restaurant_manager.model.Role;
import com.travishuy.restaurant_manager.restaurant_manager.model.User;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.Oauth2UserInfo;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.request.LoginRequest;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.request.SignUpRequest;
import com.travishuy.restaurant_manager.restaurant_manager.oauth2.response.AuthResponse;
import com.travishuy.restaurant_manager.restaurant_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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
        user.setRole(Set.of(Role.ROLE_MANAGER));
        user.setProvider(AuthProvider.LOCAL);

        User savedUser = userRepository.save(user);
        String token = tokenProvider.generateToken(savedUser);
        String refreshToken = tokenProvider.generateRefeshToken(savedUser);


        return new AuthResponse(
                token,
                refreshToken,
                "Đăng ký tài khoản thành công",
                savedUser.getEmail(),
                savedUser.getRole().toString(),
                savedUser.getName(),
                savedUser.getId(),
                false
        );
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
        String refreshToken = tokenProvider.generateRefeshToken(user);


        return new AuthResponse(
                token,
                refreshToken,
                "Xác thực user thành công",
                user.getEmail(),
                user.getRole().toString(),
                user.getName(),
                user.getId(),
                false
        );
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
        String refreshToken = tokenProvider.generateRefeshToken(user);


        return new AuthResponse(
                token,
                refreshToken,
                "Đăng nhập thành công",
                user.getEmail(),
                user.getRole().toString(),
                user.getName(),
                user.getId(),
                false
        );
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
        user.setRole(Set.of(Role.ROLE_EMPLOYEE));
        return userRepository.save(user);
    }

    /**
     * Logs out a user by invalidating their tokens.
     *
     * @param token The JWT token to invalidate
     * @return The response confirming successful logout
     */
    public AuthResponse logoutUser(String token){
        try{
            if(token==null){
                throw new IllegalArgumentException("Token not found");
            }
            String actualToken = token;

            if(token!=null && token.startsWith("Bearer ")){
                actualToken = token.substring(7);
            }

            tokenProvider.invalidateToken(actualToken);

            return new AuthResponse(null,
                    null,"Đăng xuất thành công",
                    null,null,null,null,null,false);
        }
        catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new RuntimeException("Failed to update order status: " + e.getMessage(), e);
        }
    }
    public AuthResponse registerAdmin(SignUpRequest request){

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

//        // Get the currently authenticated user (if any)
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        // If we're enforcing that only admins can create other admins,
//        // uncomment this block and modify as needed
//
//        if (authentication != null && authentication.isAuthenticated()
//                && !authentication.getAuthorities().stream()
//                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
//            throw new RuntimeException("Không có quyền truy cập tạo tài khoản admin");
//        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setRole(Set.of(Role.ROLE_ADMIN));
        user.setProvider(AuthProvider.LOCAL);

        User savedUser = userRepository.save(user);
        String token = tokenProvider.generateToken(savedUser);
        String refreshToken = tokenProvider.generateRefeshToken(savedUser);

        return new AuthResponse(
                token,
                refreshToken,
                "Đăng ký admin thành công",
                user.getEmail(),
                user.getRole().toString(),
                user.getName(),
                user.getId(),
                true
        );
    }

    /**
     * Authenticates an admin user.
     *
     * @param request The request to authenticate the admin user
     * @return The response to the authentication request
     * @throws RuntimeException if the user does not exist, password is incorrect, or user is not an admin
     */
    public AuthResponse authenticateAdmin(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new RuntimeException("Mật khẩu không khớp");
        }

        if (!user.getRole().contains(Role.ROLE_ADMIN)) {
            throw new RuntimeException("Không có quyền truy cập trang admin");
        }

        String token = tokenProvider.generateToken(user);
        String refreshToken = tokenProvider.generateRefeshToken(user);

        return new AuthResponse(
                token,
                refreshToken,
                "Xác thực admin thành công",
                user.getEmail(),
                user.getRole().toString(),
                user.getName(),
                user.getId(),
                true
        );

    }
    /**
     * Retrieves all users in the system.
     *
     * @return List of all users
     */
    public List<User> getAllUsers(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication !=null && authentication.isAuthenticated() && !authentication.getAuthorities().stream()
                .anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"))
        )
        {
            throw new RuntimeException("Không có quyền truy cập danh sách người dùng");
        }
        return userRepository.findAll();
    }
}
