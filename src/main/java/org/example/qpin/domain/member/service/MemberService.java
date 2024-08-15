package org.example.qpin.domain.member.service;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.example.qpin.domain.insurance.entity.Insurance;
import org.example.qpin.domain.member.dto.request.MemberEditRequestDto;
import org.example.qpin.domain.member.dto.request.SignupRequestDto;
import org.example.qpin.domain.member.dto.response.LoginResponseDto;
import org.example.qpin.domain.member.dto.response.MemberEditInfoResponseDto;
import org.example.qpin.domain.member.dto.response.MemberInfoResponseDto;
import org.example.qpin.domain.member.dto.response.MemberQrDto;
import org.example.qpin.domain.member.entity.Member;
import org.example.qpin.domain.qr.entity.Qr;
import org.example.qpin.global.common.repository.InsuranceRepository;
import org.example.qpin.global.common.repository.MemberRepository;
import org.example.qpin.global.common.repository.QrRepository;
import org.example.qpin.global.common.response.ResponseCode;
import org.example.qpin.global.exception.BadRequestException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.qpin.global.exception.ExceptionCode.DUPLICATED_ADMIN_USERID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final QrRepository qrRepository;
    private final MemberRepository memberRepository;
    private final InsuranceRepository insuranceRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void signup(SignupRequestDto request) throws WriterException, ParseException {

        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();
        String phoneNumber = request.getPhoneNumber();

        if(memberRepository.existsByEmail(email)){
            throw new BadRequestException(DUPLICATED_ADMIN_USERID);
        }

        LocalDateTime now = LocalDateTime.now();

//        Member data = new Member(username, bCryptPasswordEncoder.encode(password), "ROLE_ADMIN", email, phoneNumber);

            Member newMember = Member.builder()
                .name(username)
                .password(bCryptPasswordEncoder.encode(password))
                .role("ROLE_ADMIN")
                .email(email)
                .phoneNumber(phoneNumber)
                .lastLogin(now)
                .build();

        memberRepository.save(newMember);
    }

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
