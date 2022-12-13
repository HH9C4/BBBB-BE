package com.sdy.bbbb.service.social;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import com.sdy.bbbb.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final KakaoAccountService kakaoAccountService;
    private final NaverAccountService naverAccountService;
    private final AccountRepository accountRepository;

    @Transactional
    public GlobalResponseDto<String> signOut(Account account) {
        account = accountRepository.findById(account.getId()).orElseThrow(() -> new CustomException(ErrorCode.NotFoundUser));
        if(account.getKakaoId() != null) {
            try {
                kakaoAccountService.kakaoSignout(account);
            } catch (JsonProcessingException e) {
                throw new CustomException(ErrorCode.FailKakaoSignout);
            }
        }else if (account.getNaverId() != null) {
            try {
                naverAccountService.naverSignout(account);
            } catch (IOException e) {
                throw new CustomException(ErrorCode.FailKakaoSignout);
            }
        }
        account.signOut();
        return GlobalResponseDto.ok("탈퇴완료", null);
    }
}
