package org.example.dto.user.registration;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.example.dto.user.registration.annotation.FieldMatch;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@FieldMatch(first = "password",
        second = "repeatPassword",
        message = "{validation.password-repeat-password.not-match}")
@Accessors(chain = true)
public class UserRequestDto {
    @NotBlank
    @Email(message = "{validation.email.invalid}")
    private String email;
    @AssertTrue(message = "{validation.age.invalid}")
    private Boolean olderThanEighteen;
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z]).{8,35}$",
            message = "{validation.password.format}"
    )
    private String password;
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z]).{8,35}$",
            message = "{validation.password.format}"
    )
    private String repeatPassword;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    @Length(min = 8, max = 13)
    @Pattern(
            regexp = "^(\\+380|0)\\d{9}$",
            message = "{validation.phone.invalid}"
    )
    private String phoneNumber;
}
