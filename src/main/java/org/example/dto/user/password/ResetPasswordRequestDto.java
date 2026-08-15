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
        message = "Password and repeated password do not match")
@Accessors(chain = true)
public class ResetPasswordRequestDto {

    @NotBlank
    private String token;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z]).{8,35}$",
            message = "Password must contain at least 8 characters, at least one uppercase and one lowercase letter"
    )
    private String newPassword;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z]).{8,35}$",
            message = "Password must contain at least 8 characters, at least one uppercase and one lowercase letter"
    )
    private String repeatPassword;
}
