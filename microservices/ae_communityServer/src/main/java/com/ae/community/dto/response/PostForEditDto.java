package com.ae.community.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PostForEditDto {
    @ApiModelProperty(value = "조회 요청한 게시글 id")
    private Long postIdx;
    @ApiModelProperty(value = "조회 요청한 게시글 게시판 종류")
    private String boardName;
    @ApiModelProperty(value = "조회 요청한 게시글 제목")
    private String title;
    @ApiModelProperty(value = "조회 요청한 게시글 내용")
    private String content;
    @ApiModelProperty(value = "조회 요청한 게시글 작성자의 userId")
    private Long userIdx;
    @ApiModelProperty(value = "조회 요청한 게시글 이미지 리스트")
    private List<ImagesListDto> imagesLists;
}
