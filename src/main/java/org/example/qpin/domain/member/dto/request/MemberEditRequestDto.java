package org.example.qpin.domain.member.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberEditRequestDto {

    private String name;
    private String email;
}
