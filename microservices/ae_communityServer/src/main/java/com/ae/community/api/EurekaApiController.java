package com.ae.community.api;

import com.ae.community.dto.request.PostNicknameReqDto;
import com.ae.community.dto.response.StringResponseDto;
import com.ae.community.service.CommentService;
import com.ae.community.service.PostingService;
import com.ae.community.validation.UserValidationController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@Api(tags = "Eureka API", description = "유레카 서버 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/eureka")
public class EurekaApiController {
    private final PostingService postingService;
    private final CommentService commentService;
    private final UserValidationController userValidationController;

    /**
     * [Post] 50-1 닉네임 수정에 따른 업데이트 API
     * */
    @ApiOperation(value = "[POST] 50-1 닉네임 수정에 따른 업데이트   ", notes = "userIdx에 해당하는 게시글과 댓글을 변경된 닉네임으로 업데이트합니다.")
    @PostMapping("/{userIdx}")
    public ResponseEntity<?> updateNickname(@PathVariable(value = "userIdx") Long userIdx,
                                            @RequestBody PostNicknameReqDto request){

        postingService.updateNickname(request.getNickname(), userIdx);
        commentService.updateNickname(request.getNickname(), userIdx);

        return ResponseEntity.ok().body(new StringResponseDto("업데이트 되었습니다."));

    }
}
