package com.vighnaharta.nursery.dto;

import com.vighnaharta.nursery.enums.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    private String username;
    private String password;
    private Role role;
}
