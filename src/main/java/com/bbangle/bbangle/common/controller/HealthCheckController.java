package com.bbangle.bbangle.common.controller;

import static com.bbangle.bbangle.exception.BbangleErrorCode.INTERNAL_SERVER_ERROR;

import com.bbangle.bbangle.common.dto.CommonResult;
import com.bbangle.bbangle.common.service.ResponseService;
import com.bbangle.bbangle.exception.BbangleException;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/health")
public class HealthCheckController {

  private final ResponseService responseService;

  @GetMapping
  public CommonResult ping() {
    return responseService.getSingleResult("pong");
  }

  @GetMapping("/list")
  public CommonResult pingList() {
    return responseService.getListResult(List.of("pong1", "pong2"));
  }

  @GetMapping("/fail")
  public CommonResult pingFail() {
    return responseService.getFailResult("테스트 실패", -1);
  }

  @GetMapping("/exception/bbangle")
  public CommonResult pingException() {
    throw new BbangleException(INTERNAL_SERVER_ERROR);
  }

  @GetMapping("/exception/normal")
  public CommonResult pingException2() {
    throw new RuntimeException("예상치못한 예외발생 테스트");
  }

  @GetMapping("/validation")
  public CommonResult validation(
      @RequestParam(value = "test") @NotEmpty String test
  ) {
    return responseService.getSingleResult(test);
  }
}
