package com.errday.boardservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "boards")
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    private String title;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @Getter(AccessLevel.NONE)
    private User user;

    @Getter(AccessLevel.NONE)
    @Column(name = "user_id")
    private Long userId;

    public Board(String title, String content, Long userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

    public Long getUserId() {
        return user.getUserId();
    }

    public String getUserName() {
        return user.getName();
    }
}
