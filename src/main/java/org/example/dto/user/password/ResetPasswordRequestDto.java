package org.example.dto.user.password;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.example.dto.user.registration.annotation.FieldMatch;

@Getter
@Setter
@FieldMatch(first = "newPassword",
        second = "repeatPassword",
        message = "{validation.password-repeat-password.not-match}")
@Accessors(chain = true)
public class ResetPasswordRequestDto {

    @NotBlank
    private String token;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z]).{8,35}$",
            message = "{validation.password.format}"
    )
    private String newPassword;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z]).{8,35}$",
            message = "{validation.password.format}"
    )
    private String repeatPassword;
}
