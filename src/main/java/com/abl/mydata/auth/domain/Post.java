package com.abl.mydata.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity // 프로젝트 실행시 DB에 자동생성
@Data
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(
        name = "POST_SEQ_GENERATOR",
        sequenceName = "POST_SEQ", // 매핑할 데이터베이스 시퀀스 이름
        initialValue = 1,
        allocationSize = 1)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_SEQ_GENERATOR")
    @Column(name = "POST_ID")
    private Integer postId;

    // User : Post -> 1 : N, Main : Sub -> Parent : Child
    // Post가 Many User가 One
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @JsonIgnore // 외부에 비공개
    private User user;

    private String description;
}
