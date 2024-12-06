package com.konkuk.chapterkeep.post.service;

import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.common.response.exception.GeneralException;
import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.domain.Post;
import com.konkuk.chapterkeep.domain.posts.EssayPost;
import com.konkuk.chapterkeep.likes.repository.LikesRepository;
import com.konkuk.chapterkeep.member.repository.MemberRepository;
import com.konkuk.chapterkeep.member.service.MemberService;
import com.konkuk.chapterkeep.post.dto.EssayPostListResDto;
import com.konkuk.chapterkeep.post.dto.EssayPostReqDto;
import com.konkuk.chapterkeep.post.dto.EssayPostResDto;
import com.konkuk.chapterkeep.post.repository.EssayPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EssayPostService {

    private final LikesRepository likesRepository;
    private final EssayPostRepository essayPostRepository;

    public EssayPostResDto createEssayPost(Member member, EssayPostReqDto essayPostReqDto) {
        try {
            // 요청 데이터 유효성 검사
            validateEssayPostRequest(essayPostReqDto);

            Long memberId = member.getMemberId();

            EssayPost essayPost = EssayPost.createEssayPost(
                    member,
                    essayPostReqDto.getPostTitle(),
                    essayPostReqDto.isAnonymous(),
                    essayPostReqDto.getContent()
            );
            essayPostRepository.save(essayPost);

            return EssayPostResDto.builder()
                    .memberId(memberId)
                    .nickname(member.getNickname())
                    .profileUrl(member.getProfileUrl())
                    .postId(essayPost.getPostId())
                    .postTitle(essayPost.getTitle())
                    .anonymous(essayPost.isAnonymous())
                    .content(essayPost.getContent())
                    .createdAt(essayPost.getCreatedAt())
                    .modifiedAt(essayPost.getModifiedAt())
                    .likesCount(likesRepository.countByPost_PostId(essayPost.getPostId()))
                    .build();
        }catch (GeneralException e){
            throw e;
        }catch (Exception e){
            throw new GeneralException(Code.INTERNAL_ERROR, "백일장 게시글 저장 도중 알 수 없는 오류 발생");
        }
    }

    public List<EssayPostListResDto> getAllEssayPost() {
        try {
            List<EssayPost> essayPosts = essayPostRepository.findAll();

            return essayPosts.stream()
                    .map(post -> EssayPostListResDto.builder()
                            .postTitle(post.getTitle())
                            .nickname(post.getMember().getNickname())
                            .likesCount(likesRepository.countByPost_PostId(post.getPostId()))
                            .build()
                    )
                    .toList();
        }catch (GeneralException e){
            throw e;
        }catch (Exception e){
            throw new GeneralException(Code.INTERNAL_ERROR, "백일장 게시글 조회 도중 알 수 없는 오류 발생");
        }
    }


    public EssayPostResDto getEssayPost(Long postId) {
        try{
            if (postId == null || postId <= 0) {
                throw new GeneralException(Code.INVALID_INPUT_VALUE, "유효하지 않은 게시글 ID: " + postId);
            }

            Post post = essayPostRepository.findById(postId)
                    .orElseThrow(() -> new GeneralException(Code.NOT_FOUND, "게시글을 찾을 수 없음: " + postId));

            try {
                long likesCount = likesRepository.countByPost_PostId(postId);
                return EssayPostResDto.fromEntity(post, likesCount);
            } catch (Exception e) {
                throw new GeneralException(Code.INTERNAL_ERROR, "좋아요 수 계산 중 오류 발생: " + e.getMessage());
            }

        }catch (GeneralException e){
            throw e;
        }catch (Exception e){
            throw new GeneralException(Code.INTERNAL_ERROR, "백일장 게시글 조회 도중 알 수 없는 오류 발생");
        }
    }


    public EssayPostResDto updateEssayPost(Long postId, EssayPostReqDto essayPostReqDto) {
        try {

            // 요청 데이터 유효성 검사
            validateEssayPostRequest(essayPostReqDto);

            EssayPost essayPost = essayPostRepository.findById(postId)
                    .orElseThrow(() -> new GeneralException(Code.NOT_FOUND, "게시글을 찾을 수 없음: " + postId));

            if (essayPost != null) {
                essayPost.update(
                        essayPostReqDto.getPostTitle(),
                        essayPostReqDto.isAnonymous(),
                        essayPostReqDto.getContent()
                );
            } else {
                throw new GeneralException(Code.POST_MISMATCH, "해당 게시글은 백일장 게시글이 아님");
            }

            essayPostRepository.save(essayPost);
            long likesCount = likesRepository.countByPost_PostId(postId);

            return EssayPostResDto.fromEntity(essayPost, likesCount);
        }catch (GeneralException e){
            throw e;
        }catch (Exception e){
            throw new GeneralException(Code.INTERNAL_ERROR, "수정된 백일장 게시글 저장 도중 알 수 없는 오류 발생");
        }
    }

    public void deletePost(Long postId) {
        try {
            if (likesRepository.existsByPost_PostId(postId)) {
                likesRepository.deleteByPost_PostId(postId);
            }
            essayPostRepository.deleteById(postId);
        }catch (GeneralException e){
            throw e;
        }catch (Exception e){
            throw new GeneralException(Code.INTERNAL_ERROR, "백일장 게시글 삭제 도중 알 수 없는 오류 발생");
        }
    }

    public List<EssayPostListResDto> searchEssayPost(String keyword) {
        try {

            if (keyword == null || keyword.trim().isEmpty()) {
                throw new GeneralException(Code.INVALID_INPUT_VALUE, "유효하지 않은 검색어: " + keyword);
            }

            List<EssayPost> posts = essayPostRepository.findByTitleContainingOrContentContaining(keyword, keyword);

            return posts.stream()
                    .map(post -> EssayPostListResDto.builder()
                            .postTitle(post.getTitle())
                            .nickname(post.getMember().getNickname())
                            .likesCount(likesRepository.countByPost_PostId(post.getPostId()))
                            .build())
                    .toList();
        }catch (GeneralException e){
            throw e;
        }catch (Exception e){
            throw new GeneralException(Code.INTERNAL_ERROR, "백일장 게시글 검색 도중 알 수 없는 오류 발생");
        }

    }

    private void validateEssayPostRequest(EssayPostReqDto essayPostReqDto) {
        if (essayPostReqDto == null) {
            throw new GeneralException(Code.INVALID_INPUT_VALUE, "요청 데이터가 비어 있습니다.");
        }

        // 제목 검증
        if (essayPostReqDto.getPostTitle() == null || essayPostReqDto.getPostTitle().trim().isEmpty()) {
            throw new GeneralException(Code.INVALID_INPUT_VALUE, "게시글 제목은 필수 항목입니다.");
        }

        // 내용 검증
        if (essayPostReqDto.getContent() == null || essayPostReqDto.getContent().trim().isEmpty()) {
            throw new GeneralException(Code.INVALID_INPUT_VALUE, "게시글 내용은 필수 항목입니다.");
        }

        // 내용 길이 제한 검증
        if (essayPostReqDto.getContent().length() > 2000) {
            throw new GeneralException(Code.INVALID_INPUT_VALUE, "게시글 내용은 최대 2000자까지 작성 가능합니다.");
        }

    }



}
