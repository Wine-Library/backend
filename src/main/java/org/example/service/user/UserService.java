package org.example.service.user;

import org.example.dto.user.registration.UserRequestDto;
import org.example.dto.user.registration.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRequestDto request);

    UserResponseDto getMyInfo();

    void processForgotPassword(String email);

    void confirmEmail(String token);

    void resetPassword(String token, String newPassword);
}
