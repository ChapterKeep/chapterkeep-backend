package com.konkuk.chapterkeep.member.service;

import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.common.response.exception.GeneralException;
import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.domain.enums.Role;
import com.konkuk.chapterkeep.member.dto.SignUpReqDto;
import com.konkuk.chapterkeep.member.dto.SignUpResDto;
import com.konkuk.chapterkeep.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SignUpService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SignUpResDto signUpProcess(SignUpReqDto signUpReqDto) {

        String username = signUpReqDto.getId();
        String nickname = signUpReqDto.getNickname();

        if ( isUsernameExists(username) || isNicknameExists(nickname)){
            throw new GeneralException(Code.INVALID_INPUT_VALUE, "중복된 아이디 또는 중복된 닉네임");
        }

        Member member;
        try {
            member = Member.builder()
                    .name(username)
                    .password(bCryptPasswordEncoder.encode(signUpReqDto.getPassword())) // 암호화
                    .nickname(nickname)
                    .role(Role.USER)
                    .introduction(signUpReqDto.getIntroduction())
                    .visibility(true)
                    .profileUrl(null) // 추후 사진 업로드 구현하여 수정 필요
                    .build();
            memberRepository.save(member);
        } catch (DataAccessException e) {
            throw new GeneralException(Code.DATABASE_ERROR, "회원 정보 저장 도중 오류 발생");
        } catch (Exception e) {
            throw new GeneralException(Code.INTERNAL_ERROR, "회원 정보 저장 도중 알 수 없는 오류 발생");
        }
        return SignUpResDto.builder()
                .memberId(member.getMemberId())
                .build();
    }

    public boolean isUsernameExists(String username) {
        return memberRepository.existsByName(username);
    }
    public boolean isNicknameExists(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

}
