package com.konkuk.chapterkeep.member.service;

import com.konkuk.chapterkeep.common.response.exception.GeneralException;
import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.domain.Post;
import com.konkuk.chapterkeep.member.dto.MypagePostListDto;
import com.konkuk.chapterkeep.member.dto.MypageResDto;
import com.konkuk.chapterkeep.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.konkuk.chapterkeep.common.response.enums.Code;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MypageService {

    private final PostRepository postRepository;

    public MypageResDto getMyPage(Member member) {
        try {

            // 1. 사용자가 작성한 게시글
            List<MypagePostListDto> myPosts = getMyPosts(member).stream()
                    .limit(3) // 상위 3개만 추출
                    .toList();

            // 2. 댓글 단 게시글
            List<MypagePostListDto> commentedPosts = getCommentedPosts(member).stream()
                    .limit(3) // 상위 3개만 추출
                    .toList();

            // 3. 좋아요 누른 게시글
            List<MypagePostListDto> likedPosts = getLikedPosts(member).stream()
                    .limit(3) // 상위 3개만 추출
                    .toList();

            return MypageResDto.builder()
                    .nickname(member.getNickname())
                    .postCount((long) member.getBookReviews().size())
                    .myPosts(myPosts)
                    .commentedPosts(commentedPosts)
                    .likedPosts(likedPosts)
                    .build();
        }catch (GeneralException e){
            throw e;
        }catch (Exception e){
            throw new GeneralException(Code.INTERNAL_ERROR, "마이페이지 조회 도중 알 수 없는 오류 발생");
        }
    }

    public List<MypagePostListDto> getMyPosts(Member member) {
        try {
            Long memberId = member.getMemberId();
            return postRepository.findByMember_MemberId(memberId)
                    .stream()
                    .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt())) // 게시글 생성순으로 내림차순 정렬
                    .map(post -> MypagePostListDto.builder()
                            .title(post.getTitle())
                            .createdAt(post.getCreatedAt().toLocalDate())
                            .nickname(post.getMember().getNickname())
                            .postId(post.getPostId())
                            .build())
                    .toList();
        } catch (Exception e) {
            throw new GeneralException(Code.INTERNAL_ERROR, "사용자가 작성한 게시글을 조회하는 도중 오류 발생");
        }
    }

    public List<MypagePostListDto> getCommentedPosts(Member member) {
        try {
            return member.getComments().stream()
                    .sorted((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt())) // 댓글 생성순으로 내림차순 정렬
                    .map(comment -> {
                        Post post = comment.getPost();
                        return MypagePostListDto.builder()
                                .title(post.getTitle())
                                .createdAt(post.getCreatedAt().toLocalDate())
                                .nickname(post.getMember().getNickname())
                                .postId(post.getPostId())
                                .build();
                    })
                    .toList();
        } catch (Exception e) {
            throw new GeneralException(Code.INTERNAL_ERROR, "사용자가 댓글 단 게시글을 조회하는 도중 오류 발생");
        }
    }

    public List<MypagePostListDto> getLikedPosts(Member member) {
        try {
            return member.getLikes().stream()
                    .sorted((l1, l2) -> l2.getCreatedAt().compareTo(l1.getCreatedAt())) // 좋아요 생성순으로 내림차순 정렬
                    .map(like -> {
                        Post post = like.getPost();
                        return MypagePostListDto.builder()
                                .title(post.getTitle())
                                .createdAt(post.getCreatedAt().toLocalDate())
                                .nickname(post.getMember().getNickname())
                                .postId(post.getPostId())
                                .build();
                    })
                    .toList();
        } catch (Exception e) {
            throw new GeneralException(Code.INTERNAL_ERROR, "사용자가 좋아요 누른 게시글을 조회하는 도중 오류 발생");
        }
    }

}
