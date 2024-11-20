package com.konkuk.chapterkeep.member.service;

import com.konkuk.chapterkeep.image.service.ImageService;
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
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ImageService imageService;
    private final MemberService memberService;



    public SignUpResDto signUpProcess(SignUpReqDto signUpReqDto, MultipartFile profileImage) {

        String username = signUpReqDto.getId();
        String nickname = signUpReqDto.getNickname();

        if ( isUsernameExists(username) || isNicknameExists(nickname)){
            throw new GeneralException(Code.INVALID_INPUT_VALUE, "중복된 아이디 또는 중복된 닉네임");
        }

        String profileUrl = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            // S3에 이미지 업로드하고 URL 생성
            profileUrl = imageService.uploadImageToS3(profileImage);
        }

        Member member;
        try {
            member = Member.createMember(
                    username,
                    bCryptPasswordEncoder.encode(signUpReqDto.getPassword()), // 암호화
                    nickname,
                    signUpReqDto.getIntroduction(),
                    profileUrl,
                    Role.USER,
                    true
            );
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
    public String deleteAccountProcess(String username) {
        Long memberId = memberService.getCurrentMemberId();
        if (memberRepository.findById(memberId).equals(memberRepository.findByName(username))) {
            memberRepository.deleteByMemberId(memberId);
        }else{
            throw new GeneralException(Code.MEMBER_MISMATCH, "회원 이름이 현재 계정과 다름");
        }
        return username + " 회원 탈퇴 성공";
    }

    public boolean isUsernameExists(String username) {
        return memberRepository.existsByName(username);
    }
    public boolean isNicknameExists(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

}
