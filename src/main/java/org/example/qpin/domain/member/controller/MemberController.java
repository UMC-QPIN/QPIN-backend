package org.example.qpin.domain.member.controller;

import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.qpin.domain.login.dto.request.CustomUserDetails;
import org.example.qpin.domain.member.dto.request.LoginRequestDto;
import org.example.qpin.domain.member.dto.request.MemberEditRequestDto;
import org.example.qpin.domain.member.dto.request.SignupRequestDto;
import org.example.qpin.domain.member.dto.response.LoginResponseDto;
import org.example.qpin.domain.member.dto.response.MemberEditInfoResponseDto;
import org.example.qpin.domain.member.dto.response.MemberInfoResponseDto;
import org.example.qpin.domain.member.entity.Member;
import org.example.qpin.domain.member.service.MemberService;
import org.example.qpin.global.exception.BadRequestException;
import org.example.qpin.global.jwt.JWTUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    @PostMapping("/auth/signup")
    @Operation(summary = "회원가입", description = "회원가입 요청 API")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto request) {

        memberService.signup(request);

        return ResponseEntity.status(HttpStatus.OK).body("요청에 성공하였습니다");
    }

    @GetMapping("/member")
    @Operation(summary = "메인화면 기본 정보 조회", description = "메인 홈 화면 기본 회원 정보 보여주기 API")
    public ResponseEntity<MemberInfoResponseDto> memberInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMemberId();
        MemberInfoResponseDto response = memberService.getMemberInfo(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/member/mypage")
    @Operation(summary = "내 프로필 수정 조회", description = "내 프로필 수정 시 회원 정보 보여주기 API")
    public ResponseEntity<MemberEditInfoResponseDto> memberEditInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMemberId();
        MemberEditInfoResponseDto response = memberService.getMemberEditInfo(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/member/edit")
    @Operation(summary = "내 프로필 수정", description = "프로필 수정 API")
    public ResponseEntity<String> memberEdit(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody MemberEditRequestDto request) {

        Long memberId = userDetails.getMemberId();
        memberService.updateMemberInfo(request, memberId);

        return ResponseEntity.status(HttpStatus.OK).body("요청에 성공하였습니다");
    }

}
