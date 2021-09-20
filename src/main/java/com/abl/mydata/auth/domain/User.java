package com.abl.mydata.auth.domain;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonIgnoreProperties(value = {"password", "ssn"})
@JsonFilter("UserInfo")
@ApiModel(description = "사용자 상세 정보를 위한 도메인 객체")
@Entity // 프로젝트 실행시 DB에 자동생성 (기본키를 제외한 알파벳 순?)
@SequenceGenerator(
        name = "USER_SEQ_GENERATOR",
        sequenceName = "USER_SEQ", // 매핑할 데이터베이스 시퀀스 이름
        initialValue = 1,
        allocationSize = 1)
public class User {

    public User(Integer userId, String name, Date joinDate, String password, String ssn) {
        this.userId = userId;
        this.name = name;
        this.joinDate = joinDate;
        this.password = password;
        this.ssn = ssn;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ_GENERATOR")
    @Column(name = "USER_ID")
    private Integer userId;

    @Size(min=2, message = "이름은 2글자 이상이어야 합니다.")
    // default message [크기가 2에서 2147483647 사이여야 합니다]
    @ApiModelProperty(notes = "사용자 이름을 입력해 주세요.")
    private String name;

    @Past
    @ApiModelProperty(notes = "등록일을 입력해 주세요.")
    private Date joinDate;

    @ApiModelProperty(notes = "사용자 패스워드을 입력해 주세요.")
    private String password;
    @ApiModelProperty(notes = "사용자 주민번호를 입력해 주세요.")
    private String ssn;

    @OneToMany(mappedBy = "user") // User 1명당 Post 여러개
    private List<Post> posts;
}
