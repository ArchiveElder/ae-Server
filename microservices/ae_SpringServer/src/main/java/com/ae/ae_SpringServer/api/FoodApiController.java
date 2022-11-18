package com.ae.ae_SpringServer.api;

import com.ae.ae_SpringServer.config.BaseResponse;
import com.ae.ae_SpringServer.domain.Food;
import com.ae.ae_SpringServer.domain.User;
import com.ae.ae_SpringServer.dto.response.FoodResponseDto;
import com.ae.ae_SpringServer.dto.response.FoodTypeResponseDto;
import com.ae.ae_SpringServer.dto.response.ResResponse;
import com.ae.ae_SpringServer.service.FoodService;
import com.ae.ae_SpringServer.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;

import static com.ae.ae_SpringServer.config.BaseResponseStatus.EMPTY_JWT;
import static com.ae.ae_SpringServer.config.BaseResponseStatus.INVALID_JWT;
import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chaebbi/food")
public class FoodApiController {
    private final FoodService foodService;
    private final UserService userService;

    //[GET] 2-1 모든 음식 검색
    @GetMapping("/search-all")
    public BaseResponse<ResResponse> foods(@AuthenticationPrincipal HashMap<String,String> user) {
        String userId = user.get("userIdx");
        if(userId.equals("INVALID JWT")){
            return new BaseResponse<>(INVALID_JWT);
        }
        if(userId == null) {
            return new BaseResponse<>(EMPTY_JWT);
        }
        User jwtUser = userService.findOne(Long.valueOf(userId));
        if (jwtUser == null) {
            return new BaseResponse<>(INVALID_JWT);
        }
        List<Food> findFoods = foodService.findAllFoods();
        List<FoodTypeResponseDto> collect = findFoods.stream()
                .map(m -> new FoodTypeResponseDto(m.getId(), m.getName()))
                .collect(toList());
        return new BaseResponse<>(new ResResponse(collect.size(), collect));


    }
    //[POST] 2-2 음식 1개 검색
    @PostMapping("/search-one")
    public BaseResponse<ResResponse> foodResponse(@AuthenticationPrincipal HashMap<String,String> user, @RequestBody @Valid CreateFoodRequest request){
        String userId = user.get("userIdx");
        if(userId.equals("INVALID JWT")){
            return new BaseResponse<>(INVALID_JWT);
        }
        if(userId == null) {
            return new BaseResponse<>(EMPTY_JWT);
        }
        User jwtUser = userService.findOne(Long.valueOf(userId));
        if (jwtUser == null) {
            return new BaseResponse<>(INVALID_JWT);
        }

        List<Food> findFood = foodService.findFood(request.id);
        List<FoodResponseDto> collect = findFood.stream()
                .map(m -> new FoodResponseDto(m.getName(), m.getCapacity(), m.getCalory(), m.getCarb(), m.getPro(), m.getFat()))
                .collect(toList());
        return new BaseResponse<>(new ResResponse(collect.size(), collect));
    }

    @Data
    private static class CreateFoodRequest {
        @NotNull
        private Long id;
    }

}
