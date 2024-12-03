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
public class EssayPostService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;
    private final EssayPostRepository essayPostRepository;

    public EssayPostResDto createEssayPost(EssayPostReqDto essayPostReqDto) {

        Long memberId = memberService.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(Code.MEMBER_NOT_FOUND, "멤버를 찾을 수 없습니다: " + memberId));

        EssayPost essayPost = EssayPost.createEssayPost(
                member,
                essayPostReqDto.getTitle(),
                essayPostReqDto.isAnonymous(),
                essayPostReqDto.getContent()
        );
        essayPostRepository.save(essayPost);

        return EssayPostResDto.builder()
                .memberId(memberId)
                .nickname(member.getNickname())
                .profileUrl(member.getProfileUrl())
                .postId(essayPost.getPostId())
                .title(essayPost.getTitle())
                .anonymous(essayPost.isAnonymous())
                .content(essayPost.getContent())
                .createdAt(essayPost.getCreatedDate())
                .modifiedAt(essayPost.getModifiedDate())
                .likesCount(likesRepository.countByPost_PostId(essayPost.getPostId()))
                .build();
    }

    public List<EssayPostListResDto> getAllEssayPost() {

        try {

            List<EssayPost> essayPosts = essayPostRepository.findAll();

            if (essayPosts.isEmpty()) {
                throw new GeneralException(Code.NOT_FOUND, "게시글이 존재하지 않음");
            }
            return essayPosts.stream()
                    .map(post -> EssayPostListResDto.builder()
                            .title(post.getTitle())
                            .nickname(post.getMember().getNickname())
                            .likesCount(likesRepository.countByPost_PostId(post.getPostId()))
                            .build()
                    )
                    .toList();
        } catch (Exception e) {
            throw new GeneralException(Code.INTERNAL_ERROR, "게시글 데이터 처리 중 오류 발생: " + e.getMessage());
        }
    }


    public EssayPostResDto getEssayPost(Long postId) {
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
    }


    public EssayPostResDto updateEssayPost(Long postId, EssayPostReqDto essayPostReqDto) {

        EssayPost essayPost = essayPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없음: " + postId));

        if (essayPost != null) {
            essayPost.update(
                    essayPostReqDto.getTitle(),
                    essayPostReqDto.isAnonymous(),
                    essayPostReqDto.getContent()
            );
        } else {
            throw new IllegalArgumentException("해당 게시글은 에세이 게시글이 아닙니다.");
        }

        essayPostRepository.save(essayPost);
        long likesCount = likesRepository.countByPost_PostId(postId);

        return EssayPostResDto.fromEntity(essayPost, likesCount);
    }

    @Transactional
    public void deletePost(Long postId) {
        if (likesRepository.existsByPost_PostId(postId)) {
            likesRepository.deleteByPost_PostId(postId);
        }
        essayPostRepository.deleteById(postId);
    }

    public List<EssayPostListResDto> searchEssayPost(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            throw new GeneralException(Code.INVALID_INPUT_VALUE, "유효하지 않은 검색어: " + keyword);
        }

        List<EssayPost> posts = essayPostRepository.findByTitleContainingOrContentContaining(keyword, keyword);

        if (posts.isEmpty()) {
            throw new GeneralException(Code.NOT_FOUND, "해당 검색어에 대한 검색 결과가 존재하지 않음: " + keyword);
        }

        return posts.stream()
                .map(post -> EssayPostListResDto.builder()
                        .title(post.getTitle())
                        .nickname(post.getMember().getNickname())
                        .likesCount(likesRepository.countByPost_PostId(post.getPostId()))
                        .build())
                .toList();
    }


}
