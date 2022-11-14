package com.ae.community.validation;

import com.ae.community.exception.chaebbiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.HashMap;

import static com.ae.community.exception.CodeAndMessage.*;

@Controller
@RequiredArgsConstructor
public class UserValidationController {
    public void validateUserByUserIdxAndJwt(Long userIdx, HashMap<String, String> user) {
        if(user.get("userIdx").equals("INVALID JWT-USERIDX")) throw new chaebbiException(INVALID_JWT_USERIDX);
        if(user.equals("anonymousUser")) throw new chaebbiException(EMPTY_JWT);
        if(!user.get("userIdx").equals(userIdx.toString())) throw new chaebbiException(NOT_CORRECT_JWT_AND_PATH_VARIABLE);
        if(user.get("icon").equals("INVALID JWT-ICON")) throw new chaebbiException(INVALID_JWT_ICON);
        if(user.get("nickname").equals("INVALID JWT-NICKNAME")) throw new chaebbiException(INVALID_JWT_NICKNAME);
    }
}
