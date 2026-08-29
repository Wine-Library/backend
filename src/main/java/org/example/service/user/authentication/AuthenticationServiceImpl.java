package org.example.service.user.authentication;

import lombok.RequiredArgsConstructor;
import org.example.dto.user.login.RefreshTokenRequestDto;
import org.example.dto.user.login.UserLoginRequestDto;
import org.example.dto.user.login.UserLoginResponseDto;
import org.example.exception.InvalidTokenException;
import org.example.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto authenticate(UserLoginRequestDto request) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        String accessToken = jwtUtil.generateToken(request.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(request.getEmail());

        return new UserLoginResponseDto(accessToken, refreshToken);
    }

    public UserLoginResponseDto refreshToken(RefreshTokenRequestDto request) {
        String refreshToken = request.getRefreshToken();

        if (jwtUtil.isValidToken(refreshToken) && jwtUtil.isRefreshToken(refreshToken)) {
            String email = jwtUtil.getUsername(refreshToken);

            String newAccessToken = jwtUtil.generateToken(email);
            String newRefreshToken = jwtUtil.generateRefreshToken(email);

            return new UserLoginResponseDto(newAccessToken, newRefreshToken);
        }

        throw new InvalidTokenException("Invalid or expired refresh token: " + refreshToken);
    }
}
