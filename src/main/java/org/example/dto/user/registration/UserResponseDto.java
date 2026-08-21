package org.example.dto.user.registration;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserResponseDto {
    private Long id;
    private String email;
    private String name;
    private String surname;
    private String phoneNumber;
    private String shippingAddress;
}
