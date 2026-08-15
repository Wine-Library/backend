package org.example.dto.user.password;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ForgotPasswordRequestDto {
    @NotBlank
    @Email(message = "Email format is not correct")
    private String email;
}
