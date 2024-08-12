package org.example.qpin.domain.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.qpin.domain.member.entity.Member;
import org.example.qpin.global.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Member findById(Long memberId){
        return memberRepository.findById(memberId).orElseThrow();
    }
}
