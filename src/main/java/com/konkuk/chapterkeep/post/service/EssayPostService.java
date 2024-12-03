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

        System.out.println("DTO isAnonymous: " + essayPostReqDto.isAnonymous());
        EssayPost essayPost = EssayPost.createEssayPost(
                member,
                essayPostReqDto.getTitle(),
                essayPostReqDto.isAnonymous(),
                essayPostReqDto.getContent()
        );
        System.out.println("EssayPost isAnonymous after creation: " + essayPost.isAnonymous());
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
        List<EssayPost> essayPosts = essayPostRepository.findAll();
        return essayPosts.stream()
                .map(post -> EssayPostListResDto.builder()
                        .title(post.getTitle())
                        .nickname(post.getMember().getNickname())
                        .likesCount(likesRepository.countByPost_PostId(post.getPostId()))
                        .build()
                )
                .toList();
    }

    public EssayPostResDto getEssayPost(Long postId) {
        Post post = essayPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + postId));
        long likesCount = likesRepository.countByPost_PostId(postId);
        return EssayPostResDto.fromEntity(post, likesCount);
    }


    public EssayPostResDto updateEssayPost(Long postId, EssayPostReqDto essayPostReqDto) {

        EssayPost essayPost = essayPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + postId));

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
        List<EssayPost> posts = essayPostRepository.findByTitleContainingOrContentContaining(keyword, keyword);

        return posts.stream()
                .map(post -> EssayPostListResDto.builder()
                        .title(post.getTitle())
                        .nickname(post.getMember().getNickname())
                        .likesCount(likesRepository.countByPost_PostId(post.getPostId()))
                        .build())
                .toList();
    }

}
