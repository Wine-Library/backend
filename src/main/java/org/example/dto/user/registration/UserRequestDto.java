package org.example.dto.user.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.example.dto.user.registration.annotation.FieldMatch;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@FieldMatch(first = "password",
        second = "repeatPassword",
        message = "Password and repeated password do not match")
@Accessors(chain = true)
public class UserRequestDto {
    @NotBlank
    @Email(message = "{validation.email.invalid}")
    private String email;
    @NotNull
    private int age;
    @NotBlank
    @Length(min = 8, max = 35, message = "{validation.password.size}")
    private String password;
    @NotBlank
    @Length(min = 8, max = 35, message = "{validation.password.size}")
    private String repeatPassword;
}
