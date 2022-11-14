package com.ae.community.validation;

import com.ae.community.exception.chaebbiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import static com.ae.community.exception.CodeAndMessage.*;

@Controller
@RequiredArgsConstructor
public class UserValidationController {


    public void validateUserByJwt(String jwtUserId) {
        if(jwtUserId.equals("INVALID JWT")) throw new chaebbiException(INVALID_JWT);
        if(jwtUserId.equals("anonymousUser")) throw new chaebbiException(EMPTY_JWT);
    }

    public void compareUserIdAndJwt(Long userIdx, String jwtUserId) {
        if(!jwtUserId.equals(userIdx.toString())) throw new chaebbiException(NOT_CORRECT_JWT_AND_PATH_VARIABLE);
    }

    public void validateUserByUserIdxAndJwt(Long userIdx, String jwtUserId) {
        if(jwtUserId.equals("INVALID JWT")) throw new chaebbiException(INVALID_JWT);
        if(jwtUserId.equals("anonymousUser")) throw new chaebbiException(EMPTY_JWT);
        if(!jwtUserId.equals(userIdx.toString())) throw new chaebbiException(NOT_CORRECT_JWT_AND_PATH_VARIABLE);
    }
}
