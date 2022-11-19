package com.ae.ae_SpringServer.api;

import com.ae.ae_SpringServer.config.BaseResponse;
import com.ae.ae_SpringServer.domain.BistroV2;
import com.ae.ae_SpringServer.domain.User;
import com.ae.ae_SpringServer.dto.request.*;
import com.ae.ae_SpringServer.dto.response.*;
import com.ae.ae_SpringServer.dto.response.v2.BistroResponseDtoV2;
import com.ae.ae_SpringServer.dto.response.v2.CategoryListDtoV2;
import com.ae.ae_SpringServer.dto.response.v2.CategoryListResponseDtoV2;
import com.ae.ae_SpringServer.service.BistroService;
import com.ae.ae_SpringServer.service.BookmarkService;
import com.ae.ae_SpringServer.service.UserService;
import com.ae.ae_SpringServer.validation.BistroValidationController;
import com.ae.ae_SpringServer.validation.UserValidationController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ae.ae_SpringServer.config.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chaebbi/bistro")
public class BistroApiController {
    private final BistroService bistroService;
    private final BookmarkService bookmarkService;
    private final UserService userService;

    private final UserValidationController userValidationController;
    private final BistroValidationController bistroValidationController;

    //[POST] 6-1 음식점 중분류 조회
    @PostMapping("/bistromiddle")
    public BaseResponse<ResultResponse> middle(@AuthenticationPrincipal HashMap<String,String> user, @RequestBody @Valid MiddleRequestDto request) {
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

        if(request.getWide().isEmpty() || request.getWide().equals("")) {
            return new BaseResponse<>(POST_BISTRO_NO_WIDE);
        }
        List<BistroV2> bistros = bistroService.getMiddleV2(request.getWide());
        List<String> middles = new ArrayList<>();

        for(BistroV2 bistro : bistros) {
            middles.add(bistro.getMiddle());
        }
        return new BaseResponse<>(new ResultResponse(middles));
    }

    //[POST] 6-2 대분류,중분류별 음식점조회
    @PostMapping("/categories")
    public BaseResponse<CategoryListResponseDtoV2> categories(@AuthenticationPrincipal HashMap<String,String> user, @RequestBody @Valid CategoryRequestDto request) {
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

        if(request.getWide().isEmpty() || request.getWide().equals("")) {
            return new BaseResponse<>(POST_BISTRO_NO_WIDE);
        }

        if(request.getMiddle().isEmpty() || request.getMiddle().equals("")) {
            return new BaseResponse<>(POST_BISTRO_NO_MIDDLE);
        }
        List<BistroV2> categoryList = bistroService.getCategoryListV2(request.getWide(), request.getMiddle());
        List<BistroV2> categoryGroup = bistroService.getCategoriesV2(request.getWide(), request.getMiddle());
        List<BistroV2> bookmark = bookmarkService.findBookmarkV2(Long.valueOf(userId));

        List<CategoryListDtoV2> listDtos = new ArrayList<>();
        List<String> categories = new ArrayList<>();

        for(BistroV2 bistro : categoryList) {
            int isBookmark;
            if(bookmark.indexOf(bistro) != -1) {
                isBookmark = 1;
            } else {
                isBookmark = 0;
            }
            listDtos.add(new CategoryListDtoV2(bistro.getId().intValue(), isBookmark, bistro.getCategory(), bistro.getName(),
                    bistro.getRAddr(), bistro.getLAddr(), bistro.getTel(), bistro.getBistroUrl()));
        }

        for(BistroV2 bistro : categoryGroup) {
            categories.add(bistro.getCategory());
        }

        return new BaseResponse<>(new CategoryListResponseDtoV2(categories, listDtos.size(), listDtos));
    }

    // [POST] 6-3 지도 음식점 전체 조회
    @GetMapping("/allbistro")
    public BaseResponse<ResultResponse> allBistroV3(@AuthenticationPrincipal String userId) {
        if(userId.equals("INVALID JWT")){
            return new BaseResponse<>(INVALID_JWT);
        }
        if(userId == null) {
            return new BaseResponse<>(EMPTY_JWT);
        }
        User user = userService.findOne(Long.valueOf(userId));
        if (user == null) {
            return new BaseResponse<>(INVALID_JWT);
        }
        List<BistroV2> allBistro = bistroService.getBistroV2();
        List<BistroV2> bookmark = bookmarkService.findBookmarkV2(Long.valueOf(userId));
        List<BistroResponseDto> bistroDtos = new ArrayList<>();
        for (BistroV2 bistro : allBistro){
            int isBookmark;
            if(bookmark.indexOf(bistro) != -1) {
                isBookmark = 1;
            } else {
                isBookmark = 0;
            }
            bistroDtos.add(new BistroResponseDto(isBookmark, bistro.getId(), bistro.getName(), bistro.getRAddr(), bistro.getLAddr(),
                    bistro.getTel(), bistro.getMenu(), Double.parseDouble(bistro.getLa()), Double.parseDouble(bistro.getLo()), bistro.getBistroUrl(), bistro.getMainCategory(),
                    bistro.getMiddleCategory(), bistro.getWide(), bistro.getMiddle()));
        }
        return new BaseResponse<>(new ResultResponse(bistroDtos));
    }

    // [POST] 6-4 음식점 대분류 카테고리 검색
    @PostMapping("/category-main")
    public ResponseEntity<?> getCategoryMain(@AuthenticationPrincipal HashMap<String,String> user, @RequestBody PostCategoryMainReqDto request) {
        String userId = user.get("userIdx");
        //validation 로직
        userValidationController.validateuser(Long.valueOf(userId));
        bistroValidationController.validateCategoryMain(request.getMainCategory());

        List<BistroV2> bistroList = bistroService.getCategoryMain(request.getMainCategory());
        List<BistroV2> bookmark = bookmarkService.findBookmarkV2(Long.valueOf(userId));

        List<MainCategoryListDto> listDtos = new ArrayList<>();

        for(BistroV2 bistro : bistroList) {
            int isBookmark;
            if(bookmark.indexOf(bistro) != -1) {
                isBookmark = 1;
            } else {
                isBookmark = 0;
            }
            listDtos.add(new MainCategoryListDto(bistro.getId(), isBookmark, bistro.getName(), bistro.getRAddr(), bistro.getLAddr(), bistro.getTel(), bistro.getMenu(),
                    bistro.getLa(), bistro.getLa(), bistro.getBistroUrl(), bistro.getMiddleCategory()));
        }

        return ResponseEntity.ok().body(listDtos);
    }

    // [POST] 6-5 음식점 대분류, 중분류 카테고리별 검색
    @PostMapping("/category-middle")
    public ResponseEntity<?> getCategoryMiddle(@AuthenticationPrincipal HashMap<String,String> user, @RequestBody PostCategoryMiddleReqDto request) {
        String userId = user.get("userIdx");
        //validation 로직
        userValidationController.validateuser(Long.valueOf(userId));
        bistroValidationController.validateCategoryMain(request.getMainCategory());
        bistroValidationController.validateCategoryMiddle(request.getMiddleCategory());

        List<BistroV2> bistroList = bistroService.getCategoryMiddle(request.getMainCategory(), request.getMiddleCategory());
        List<BistroV2> bookmark = bookmarkService.findBookmarkV2(Long.valueOf(userId));

        List<MiddleCategoryListDto> listDtos = new ArrayList<>();

        for(BistroV2 bistro : bistroList) {
            int isBookmark;
            if(bookmark.indexOf(bistro) != -1) {
                isBookmark = 1;
            } else {
                isBookmark = 0;
            }
            listDtos.add(new MiddleCategoryListDto(bistro.getId(), isBookmark, bistro.getName(), bistro.getRAddr(), bistro.getLAddr(), bistro.getTel(), bistro.getMenu(),
                    bistro.getLa(), bistro.getLa(), bistro.getBistroUrl()));
        }

        return ResponseEntity.ok().body(listDtos);
    }

    // [POST] 6-6 음식점 ‘장소(지역) 대분류& 장소(지역) 중분류& 카테고리 대분류’ 검색
    @PostMapping("/bistro-category-main")
    public ResponseEntity<?> getBistroMain(@AuthenticationPrincipal HashMap<String,String> user, @RequestBody PostBistroMainReqDto request) {
        String userId = user.get("userIdx");
        //validation 로직
        userValidationController.validateuser(Long.valueOf(userId));
        bistroValidationController.validateSiteWide(request.getSiteWide());
        bistroValidationController.validateSiteMiddle(request.getSiteMiddle());
        bistroValidationController.validateCategoryMain(request.getMainCategory());

        List<BistroV2> bistroList = bistroService.getBistroMain(request.getSiteWide(), request.getSiteMiddle(), request.getMainCategory());
        List<BistroV2> bookmark = bookmarkService.findBookmarkV2(Long.valueOf(userId));

        List<MainCategoryListDto> listDtos = new ArrayList<>();

        for(BistroV2 bistro : bistroList) {
            int isBookmark;
            if(bookmark.indexOf(bistro) != -1) {
                isBookmark = 1;
            } else {
                isBookmark = 0;
            }
            listDtos.add(new MainCategoryListDto(bistro.getId(), isBookmark, bistro.getName(), bistro.getRAddr(), bistro.getLAddr(), bistro.getTel(), bistro.getMenu(),
                    bistro.getLa(), bistro.getLa(), bistro.getBistroUrl(), bistro.getMiddleCategory()));
        }

        return ResponseEntity.ok().body(listDtos);
    }

    // [POST] 6-7 음식점 ‘장소(지역) 대분류& 장소(지역) 중분류& 카테고리 대분류&카테고리 중분류’ 검색
    @PostMapping("/bistro-category-middle")
    public ResponseEntity<?> getBistroMiddle(@AuthenticationPrincipal HashMap<String,String> user, @RequestBody PostBistroMiddleReqDto request) {
        String userId = user.get("userIdx");
        //validation 로직
        userValidationController.validateuser(Long.valueOf(userId));
        bistroValidationController.validateSiteWide(request.getSiteWide());
        bistroValidationController.validateSiteMiddle(request.getSiteMiddle());
        bistroValidationController.validateCategoryMain(request.getMainCategory());
        bistroValidationController.validateCategoryMiddle(request.getMiddleCategory());

        List<BistroV2> bistroList = bistroService.getBistroMiddle(request.getSiteWide(), request.getSiteMiddle(), request.getMainCategory(), request.getMiddleCategory());
        List<BistroV2> bookmark = bookmarkService.findBookmarkV2(Long.valueOf(userId));

        List<MiddleCategoryListDto> listDtos = new ArrayList<>();

        for(BistroV2 bistro : bistroList) {
            int isBookmark;
            if(bookmark.indexOf(bistro) != -1) {
                isBookmark = 1;
            } else {
                isBookmark = 0;
            }
            listDtos.add(new MiddleCategoryListDto(bistro.getId(), isBookmark, bistro.getName(), bistro.getRAddr(), bistro.getLAddr(), bistro.getTel(), bistro.getMenu(),
                    bistro.getLa(), bistro.getLa(), bistro.getBistroUrl()));
        }

        return ResponseEntity.ok().body(listDtos);
    }

    // [POST] 6-8 음식점 ‘장소(지역) 대분류&카테고리 대분류’ 검색
    @PostMapping("/site-wide-category-main")
    public ResponseEntity<?> getSiteWideMain(@AuthenticationPrincipal HashMap<String,String> user, @RequestBody PostSiteWideMainReqDto request) {
        String userId = user.get("userIdx");
        //validation 로직
        userValidationController.validateuser(Long.valueOf(userId));
        bistroValidationController.validateSiteWide(request.getSiteWide());
        bistroValidationController.validateCategoryMain(request.getMainCategory());

        List<BistroV2> bistroList = bistroService.getSiteWideMain(request.getSiteWide(), request.getMainCategory());
        List<BistroV2> bookmark = bookmarkService.findBookmarkV2(Long.valueOf(userId));

        List<MainCategoryListDto> listDtos = new ArrayList<>();

        for(BistroV2 bistro : bistroList) {
            int isBookmark;
            if(bookmark.indexOf(bistro) != -1) {
                isBookmark = 1;
            } else {
                isBookmark = 0;
            }
            listDtos.add(new MainCategoryListDto(bistro.getId(), isBookmark, bistro.getName(), bistro.getRAddr(), bistro.getLAddr(), bistro.getTel(), bistro.getMenu(),
                    bistro.getLa(), bistro.getLa(), bistro.getBistroUrl(), bistro.getMiddleCategory()));
        }

        return ResponseEntity.ok().body(listDtos);
    }
}
