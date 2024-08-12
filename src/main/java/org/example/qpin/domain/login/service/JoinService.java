package org.example.qpin.domain.login.service;

import lombok.RequiredArgsConstructor;
import org.example.qpin.domain.login.dto.request.JoinDto;
import org.example.qpin.domain.member.entity.Member;
import org.example.qpin.global.common.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDto joinDto){

        String username = joinDto.getUsername();
        String password = joinDto.getPassword();
        String email = joinDto.getEmail();
        String phoneNumber = joinDto.getPhoneNumber();
        Boolean isExist = memberRepository.existsByName(username);

        if(isExist){
            return;
        }

        Member data = new Member(username, bCryptPasswordEncoder.encode(password), "ROLE_ADMIN", email, phoneNumber);

        memberRepository.save(data);

    }
}
