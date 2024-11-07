package com.konkuk.chapterkeep.controller;

import com.konkuk.chapterkeep.common.response.dto.DataResponseDto;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.dto.member.SignUpReqDto;
import com.konkuk.chapterkeep.dto.member.SignUpResDto;
import com.konkuk.chapterkeep.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    @GetMapping("/check-id")
    public DataResponseDto<Boolean> checkId(@RequestParam String id) {
        boolean isExists = signUpService.isUsernameExists(id); // 존재하면 true
        if (isExists)
            return new DataResponseDto<>(isExists, Code.OK,"이미 존재하는 아이디");
        else
            return new DataResponseDto<>(isExists, Code.OK,"사용 가능한 아이디");
    }
    @GetMapping("/check-nickname")
    public DataResponseDto<Boolean> checkNickname(@RequestParam String nickname) {
        boolean isExists = signUpService.isNicknameExists(nickname); // 존재하면 true
        if (isExists)
            return new DataResponseDto<>(isExists, Code.OK,"이미 존재하는 닉네임");
        else
            return new DataResponseDto<>(isExists, Code.OK,"사용 가능한 닉네임");
    }
    @PostMapping("/signup")
    public DataResponseDto<SignUpResDto> signUp(@RequestBody SignUpReqDto signUpReqDto){

        SignUpResDto response = signUpService.signUpProcess(signUpReqDto);

        return new DataResponseDto<>(response, Code.OK);
    }


}
