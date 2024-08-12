package org.example.qpin.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.example.qpin.domain.insurance.entity.Insurance;
import org.example.qpin.domain.member.dto.request.MemberEditRequestDto;
import org.example.qpin.domain.member.dto.response.MemberEditInfoResponseDto;
import org.example.qpin.domain.member.dto.response.MemberInfoResponseDto;
import org.example.qpin.domain.member.dto.response.MemberQrDto;
import org.example.qpin.domain.member.entity.Member;
import org.example.qpin.domain.qr.entity.Qr;
import org.example.qpin.global.common.repository.InsuranceRepository;
import org.example.qpin.global.common.repository.MemberRepository;
import org.example.qpin.global.common.repository.QrRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final QrRepository qrRepository;
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
        List<Qr> qrList = qrRepository.findAllByMember(member);
        List<MemberQrDto> memberQrDtoList = qrList.stream()
            .map(qr ->new MemberQrDto(
                    qr.getQrImage(),
                    qr.getMemo(),
                    qr.getSafePhoneNumber().getSafePhoneNumber(),
                    qr.getCreatedAt().toLocalDate()
            )).collect(Collectors.toList());

        return MemberInfoResponseDto.builder()
                .name(member.getName())
                .memberQrDtoList(memberQrDtoList)
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
