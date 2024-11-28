package com.konkuk.chapterkeep.post.service;

import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.common.response.exception.GeneralException;
import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.domain.Post;
import com.konkuk.chapterkeep.domain.posts.EssayContestPost;
import com.konkuk.chapterkeep.likes.repository.LikesRepository;
import com.konkuk.chapterkeep.member.repository.MemberRepository;
import com.konkuk.chapterkeep.member.service.MemberService;
import com.konkuk.chapterkeep.post.dto.EssayPostReqDto;
import com.konkuk.chapterkeep.post.dto.EssayPostResDto;
import com.konkuk.chapterkeep.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EssayPostService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;
    private final PostRepository postRepository;

    public EssayPostResDto createPost(EssayPostReqDto essayPostReqDto) {

        Long memberId = memberService.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(Code.MEMBER_NOT_FOUND, "멤버를 찾을 수 없습니다: " + memberId));

        EssayContestPost essayContestPost = EssayContestPost.createEssayPost(
                member,
                essayPostReqDto.getTitle(),
                essayPostReqDto.isAnonymous(),
                essayPostReqDto.getContent()
        );
        postRepository.save(essayContestPost);

        return EssayPostResDto.builder()
                .memberId(memberId)
                .nickname(member.getNickname())
                .profileUrl(member.getProfileUrl())
                .postId(essayContestPost.getPostId())
                .title(essayContestPost.getTitle())
                .isAnonymous(essayContestPost.getIsAnonymous())
                .content(essayContestPost.getContent())
                .createdAt(essayContestPost.getCreatedDate())
                .modifiedAt(essayContestPost.getModifiedDate())
                .likesCount(likesRepository.countByPost_PostId(essayContestPost.getPostId()))
                .build();
    }

    @Transactional
    public EssayPostResDto updateEssayPost(Long postId, EssayPostReqDto essayPostReqDto) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + postId));

        if (post instanceof EssayContestPost) {
            ((EssayContestPost) post).update(
                    essayPostReqDto.getTitle(),
                    essayPostReqDto.isAnonymous(),
                    essayPostReqDto.getContent()
            );
        } else {
            throw new IllegalArgumentException("해당 게시글은 에세이 게시글이 아닙니다.");
        }

        postRepository.save(post);
        long likesCount = likesRepository.countByPost_PostId(postId);

        return EssayPostResDto.fromEntity(post, likesCount);
    }

    // TODO : 좋아요 눌린 독서 기록이 삭제되지 않는 오류 - 독서 기록 삭제 이전에 해당 독서 기록에 눌린 좋아요 엔티티 먼저 삭제 (cascade)
    // 양방향으로 전환 or 독서 기록 삭제 이전에 좋아요 엔티티 먼저 삭제 되도록 코드 상에서 작성
    public void deletePost(Long postId) {
        if (likesRepository.existsByPost_PostId(postId)) {
            likesRepository.deleteByPost_PostId(postId);
        }
        postRepository.deleteById(postId);
    }

}
