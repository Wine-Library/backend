package org.example.service.user;

import org.example.dto.user.registration.UserRequestDto;
import org.example.dto.user.registration.UserResponseDto;
import org.example.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRequestDto request)
            throws RegistrationException;

    UserResponseDto getMyInfo();
}
