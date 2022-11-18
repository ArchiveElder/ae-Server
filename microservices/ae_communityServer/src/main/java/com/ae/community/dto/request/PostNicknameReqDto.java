package com.ae.community.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PostNicknameReqDto {
    @ApiModelProperty(value = "변경할 닉네임")
    private String nickname;
}
