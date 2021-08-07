package com.abl.mydata.auth.domain;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonIgnoreProperties(value = {"password", "ssn"})
@JsonFilter("UserInfo")
@ApiModel(description = "사용자 상세 정보를 위한 도메인 객체")
public class User {

    private Integer id;

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
}
