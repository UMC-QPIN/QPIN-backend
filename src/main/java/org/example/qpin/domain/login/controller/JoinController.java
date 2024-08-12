package org.example.qpin.domain.login.controller;

import lombok.RequiredArgsConstructor;
import org.example.qpin.domain.login.dto.request.JoinDto;
import org.example.qpin.domain.login.service.JoinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public ResponseEntity<String> joinProcess(JoinDto joinDto){

        joinService.joinProcess(joinDto);

        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }
}
