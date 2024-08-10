package org.example.qpin.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.qpin.domain.member.dto.request.MemberEditRequestDto;
import org.example.qpin.domain.member.dto.response.MemberInfoResponseDto;
import org.example.qpin.domain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    @GetMapping("/home/info/{memberId}")
    @Operation(summary = "메인화면 기본 정보 조회", description = "메인 홈 화면 기본 회원 정보 보여주기 API")
    public ResponseEntity<MemberInfoResponseDto> memberInfo(@PathVariable("memberId") Long memberId) {

        MemberInfoResponseDto response = memberService.getMemberInfo(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/mypage/edit/{memberId}")
    @Operation(summary = "내 프로필 수정", description = "프로필 수정 API")
    public ResponseEntity<String> memberEdit(@PathVariable("memberId") Long memberId, @RequestBody MemberEditRequestDto request) {

        memberService.updateMemberInfo(request, memberId);

        return ResponseEntity.status(HttpStatus.OK).body("수정 완료");
    }

}
