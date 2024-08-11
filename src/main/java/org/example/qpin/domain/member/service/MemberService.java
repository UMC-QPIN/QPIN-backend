package org.example.qpin.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.example.qpin.domain.insurance.entity.Insurance;
import org.example.qpin.domain.member.dto.request.MemberEditRequestDto;
import org.example.qpin.domain.member.dto.response.MemberEditInfoResponseDto;
import org.example.qpin.domain.member.dto.response.MemberInfoResponseDto;
import org.example.qpin.domain.member.entity.Member;
import org.example.qpin.global.common.repository.InsuranceRepository;
import org.example.qpin.global.common.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final InsuranceRepository insuranceRepository;

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow();
    }

    public Insurance findInsuranceById(Long insuranceId) {
        return insuranceRepository.findById(insuranceId).orElseThrow();
    }

    public MemberInfoResponseDto getMemberInfo(Long memberId) {
        Member member = findMemberById(memberId);
        return MemberInfoResponseDto.builder()
                .name(member.getName())
               // .dateList()
                .build();
    }

    @Transactional
    public void updateMemberInfo(MemberEditRequestDto request, Long memberId) {
        Member member = findMemberById(memberId);
        Insurance insurance = findInsuranceById(request.getInsuranceId());

        member.setMemberInfo(request.getImageUrl(), request.getName(), request.getEmail(), request.getPhoneNumber(), insurance);
        memberRepository.save(member);
    }

    public MemberEditInfoResponseDto getMemberEditInfo(Long memberId) {

        Member member = findMemberById(memberId);

        return MemberEditInfoResponseDto.builder()
                .imageUrl(member.getImageUrl())
                .name(member.getName())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .insuranceId(member.getInsurance().getInsuranceId())
                .build();
    }
}
