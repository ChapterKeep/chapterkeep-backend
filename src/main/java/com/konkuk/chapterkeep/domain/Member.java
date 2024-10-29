package com.konkuk.chapterkeep.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Member")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "role", nullable = false)
    private boolean role;

    @Column(name = "visibility", nullable = false)
    private boolean visibility = true;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookReview> bookReviews = new ArrayList<>();

    // BookReview 편의 메서드
    public void addBookReview(BookReview bookReview) {
        bookReviews.add(bookReview);
        bookReview.setMember(this);
    }

    public void removeBookReview(BookReview bookReview) {
        bookReviews.remove(bookReview);
        bookReview.setMember(null);
    }

    // Post 편의 메서드
    public void addPost(Post post) {
        posts.add(post);
        post.setMember(this);
    }

    public void removePost(Post post) {
        posts.remove(post);
        post.setMember(null);
    }

    // Comment 편의 메서드
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setMember(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setMember(null);
    }
}
