package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.user.registration.UserRequestDto;
import org.example.dto.user.registration.UserResponseDto;
import org.example.dto.wine.WineResponseDto;
import org.example.service.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users management",
        description = "Endpoints for managing users and their favorite wines")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get my profile info",
            description = "Get profile's info of the authorized user")
    public UserResponseDto getMyInfo() {
        return userService.getMyInfo();
    }

    @PutMapping("/me")
    @Operation(summary = "Update my profile info",
            description = "Update profile's info of the authorized user")
    public UserResponseDto updateMyInfo(@RequestBody @Valid UserRequestDto userRequestDto) {
        return userService.updateMyInfo(userRequestDto);
    }

    @PostMapping("/favorites/{wineId}")
    @Operation(summary = "Add wine to my favorites",
            description = "Add wine to the favorites list of authorized user by its id")
    @ResponseStatus(HttpStatus.CREATED)
    public void addWineToFavorites(@PathVariable Long wineId) {
        userService.addWineToFavorites(wineId);
    }

    @DeleteMapping("/favorites/{wineId}")
    @Operation(summary = "Remove wine from my favorites",
            description = "Remove wine from the favorites list of authorized user by its id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeWineFromFavorites(@PathVariable Long wineId) {
        userService.removeWineFromFavorites(wineId);
    }

    @GetMapping("/favorites")
    @Operation(summary = "Get my favorites",
            description = "Get my favorite wines")
    public Page<WineResponseDto> getFavorites(Pageable pageable) {
        return userService.getFavorites(pageable);
    }
}
