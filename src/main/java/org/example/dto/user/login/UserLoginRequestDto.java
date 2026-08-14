package org.example.dto.user.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserLoginRequestDto {
    @NotBlank
    @Email(message = "Email format is not correct")
    private String email;
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z]).{8,35}$",
            message = "Password must contain at least 8 characters, at least one uppercase and one lowercase letter"
    )
    private String password;
}
