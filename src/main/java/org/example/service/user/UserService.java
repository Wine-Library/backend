package org.example.service.user;

import org.example.dto.user.registration.UserRequestDto;
import org.example.dto.user.registration.UserResponseDto;
import org.example.dto.wine.WineResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponseDto register(UserRequestDto request);

    UserResponseDto getMyInfo();

    UserResponseDto updateMyInfo(UserRequestDto request);

    void processForgotPassword(String email);

    void confirmEmail(String token);

    void resetPassword(String token, String newPassword);

    void addWineToFavorites(Long wineId);

    void removeWineFromFavorites(Long wineId);

    Page<WineResponseDto> getFavorites(Pageable pageable);

    void resendEmail(String email);
}
