package org.example.dto.user.registration;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.example.dto.user.registration.annotation.FieldMatch;

@Getter
@Setter
@FieldMatch(first = "password",
        second = "repeatPassword",
        message = "Password and repeated password do not match")
@Accessors(chain = true)
public class UserRequestDto {
    @NotBlank
    @Email(message = "Email format is not correct")
    private String email;
    @AssertTrue(message = "User must be older than eighteen")
    private Boolean olderThanEighteen;
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z]).{8,35}$",
            message = "Password must contain at least 8 characters, at least one uppercase and one lowercase letter"
    )
    private String password;
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z]).{8,35}$",
            message = "Password must contain at least 8 characters, at least one uppercase and one lowercase letter"
    )
    private String repeatPassword;
}
