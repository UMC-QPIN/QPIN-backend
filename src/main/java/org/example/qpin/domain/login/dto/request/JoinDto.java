package org.example.qpin.domain.login.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinDto {
    private String username;

    private String password;

    private String email;

    private String phoneNumber;
}
