package com.konkuk.chapterkeep.member.controller;

import com.konkuk.chapterkeep.common.response.dto.DataResponseDto;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.member.dto.SignUpReqDto;
import com.konkuk.chapterkeep.member.dto.SignUpResDto;
import com.konkuk.chapterkeep.member.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/check-id")
    public DataResponseDto<Boolean> checkId(@RequestParam String id) {
        boolean isExists = accountService.isUsernameExists(id); // 존재하면 true
        if (isExists)
            return new DataResponseDto<>(isExists, Code.OK,"이미 존재하는 아이디");
        else
            return new DataResponseDto<>(isExists, Code.OK,"사용 가능한 아이디");
    }
    @GetMapping("/check-nickname")
    public DataResponseDto<Boolean> checkNickname(@RequestParam String nickname) {
        boolean isExists = accountService.isNicknameExists(nickname); // 존재하면 true
        if (isExists)
            return new DataResponseDto<>(isExists, Code.OK,"이미 존재하는 닉네임");
        else
            return new DataResponseDto<>(isExists, Code.OK,"사용 가능한 닉네임");
    }
    @PostMapping(value = "/signup")
    public DataResponseDto<SignUpResDto> signUp(
            @RequestPart("info") SignUpReqDto info,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage ){

        SignUpResDto response = accountService.signUpProcess(info, profileImage);

        return new DataResponseDto<>(response, Code.OK);
    }

    @DeleteMapping(value = "/withdraw/{id}")
    public DataResponseDto<String> deleteAccount(@PathVariable(name = "id") String id){
        String response = accountService.deleteAccountProcess(id);
        return new DataResponseDto<>(response, Code.OK);
    }

}
