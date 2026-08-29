package org.example.dto.user.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class RefreshTokenRequestDto {
    @NotBlank(message = "${validation.refresh-token.invalid}")
    private String refreshToken;
}
