package com.bunyaminkalkan.api.services;

import com.bunyaminkalkan.api.entities.*;
import com.bunyaminkalkan.api.exceptions.BadRequestException;
import com.bunyaminkalkan.api.repos.DepartmentRepository;
import com.bunyaminkalkan.api.repos.UniversityRepository;
import com.bunyaminkalkan.api.repos.UserRepository;
import com.bunyaminkalkan.api.requests.LoginRequest;
import com.bunyaminkalkan.api.requests.RefreshRequest;
import com.bunyaminkalkan.api.requests.RegisterRequest;
import com.bunyaminkalkan.api.responses.AuthResponse;
import com.bunyaminkalkan.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserName(),
                        loginRequest.getPassword()
                )
        );
        var user = userRepository.findByUserName(loginRequest.getUserName())
                .orElseThrow(() -> new UsernameNotFoundException(loginRequest.getUserName()));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.getByUser(user.getId());
        return new AuthResponse().builder().userId(user.getId()).accessToken(jwtToken).refreshToken(refreshToken.getToken()).message("Login successfully").build();
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        var user = new User();
        user.setUserName(registerRequest.getUserName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);
        user.setUniversity(universityRepository.findById(registerRequest.getUniversityId()).orElse(null));
        user.setDepartment(departmentRepository.findById(registerRequest.getDepartmentId()).orElse(null));
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user);
        return new AuthResponse().builder().userId(user.getId()).accessToken(jwtToken).refreshToken(refreshToken).message("Register successfully").build();
    }

    public AuthResponse refresh(RefreshRequest refreshRequest){
        RefreshToken token = refreshTokenService.getByUser(refreshRequest.getUserId());
        if (token.getToken().equals(refreshRequest.getRefreshToken())
                && !refreshTokenService.isRefreshTokenExpired(token)){

            User user = token.getUser();
            String jwtToken = jwtService.generateToken(user);

            return new AuthResponse().builder().userId(user.getId()).accessToken(jwtToken).refreshToken(refreshTokenService.createRefreshToken(user)).message("Refresh successfully").build();
        }else {
            throw new BadRequestException("Refresh Token is not valid");
        }
    }
}
