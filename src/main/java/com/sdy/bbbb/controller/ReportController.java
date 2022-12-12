package com.sdy.bbbb.controller;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.request.ReportRequestDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.service.ReportService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/api/report")
    @ApiOperation(value = "신고 기능", notes = "")
    public GlobalResponseDto<?> report(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails,
                                       @RequestBody @Valid ReportRequestDto reportRequestDto) {
        return reportService.report(userDetails.getAccount(), reportRequestDto);
    }
}
