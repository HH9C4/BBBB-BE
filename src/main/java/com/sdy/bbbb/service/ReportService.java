package com.sdy.bbbb.service;

import com.sdy.bbbb.dto.request.ReportRequestDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Comment;
import com.sdy.bbbb.entity.Post;
import com.sdy.bbbb.entity.Report;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import com.sdy.bbbb.repository.AccountRepository;
import com.sdy.bbbb.repository.CommentRepository;
import com.sdy.bbbb.repository.PostRepository;
import com.sdy.bbbb.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public GlobalResponseDto<?> report(Account account, ReportRequestDto reportRequestDto) {
        Long level = reportRequestDto.getLevel();
        Long reporterId = account.getId();
        String reportedId = reportRequestDto.getReportedId();

        if (level.equals(1L)) {
            Account account1 = accountRepository.findAccountByAccountName(reportedId).orElseThrow(
                    () -> new CustomException(ErrorCode.NotFoundUser));
            Long accountId = account1.getId();
            saveReport(reportRequestDto, reporterId, accountId);
            if (account1.getReportedCount() > 2) {
                account1.setAccountName("불량유저");
                reportRepository.deleteAllByLevelAndReportedId(level, accountId);
            } else {
                account1.setReportedCount(account1.getReportedCount() + 1);

            }

        } else if (level.equals(2L)) {
            Long postId = Long.parseLong(reportRequestDto.getReportedId());
            Post post = postRepository.findById(postId).orElseThrow(
                    () -> new CustomException(ErrorCode.NotFoundPost));
            saveReport(reportRequestDto, reporterId, postId);
            if (post.getReportedCount() > 9) {
                post.setHide(true);
                postRepository.save(post);
            } else {
                post.setReportedCount(post.getReportedCount() + 1);
            }

        } else if (level.equals(3L)) {
            Long commentId = Long.parseLong(reportRequestDto.getReportedId());
            Comment comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new CustomException(ErrorCode.NotFoundComment));
            saveReport(reportRequestDto, reporterId, commentId);
            if (comment.getReportedCount() > 9) {
                comment.setHide(true);
                commentRepository.save(comment);
            } else {
                comment.setReportedCount(comment.getReportedCount() + 1);
            }
        }


        return GlobalResponseDto.ok("신고 완료", reportRequestDto);

    }

    // report 검증 + 저장하는 함수
    private void saveReport(ReportRequestDto reportRequestDto, Long reporterId, Long reportedId) {
        if (reportRepository.existsByLevelAndReporterIdAndReportedId(reportRequestDto.getLevel(), reporterId, reportedId)) {
            throw new CustomException(ErrorCode.AlreadyReported);
        }
        Report report = new Report(reportRequestDto, reporterId, reportedId);
        reportRepository.save(report);
    }

}
