package com.bbangle.bbangle.review.controller;

import com.bbangle.bbangle.common.dto.CommonResult;
import com.bbangle.bbangle.common.service.ResponseService;
import com.bbangle.bbangle.review.dto.ReviewRequest;
import com.bbangle.bbangle.review.service.ReviewService;
import com.bbangle.bbangle.util.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final ResponseService responseService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public CommonResult makeReview(
        @ModelAttribute ReviewRequest reviewRequest,
        //@RequestPart(required = false) List<MultipartFile> photos,
        @AuthenticationPrincipal Long memberId
    ){
        reviewService.makeReview(reviewRequest, memberId);
        return responseService.getSuccessResult();
    }
}
