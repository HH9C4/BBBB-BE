package com.sdy.bbbb.entity;

import com.sdy.bbbb.dto.request.ReportRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 글인지 유저인지 댓글인지
    @Column
    private Long level;

    @Column
    private Long reporterId;

    //postId or accountId (or commentId)
    @Column
    private Long reportedId;

    // 우리가 관리해 줄 목적이 있어야 하는거 아닌가?
    @Column
    private String content;


    public Report(ReportRequestDto reportRequestDto, Account account) {
        this.level = reportRequestDto.getLevel();
        this.reporterId = account.getId();
        this.reportedId = reportRequestDto.getReportedId();
        this.content = reportRequestDto.getContent();
    }

    public Report(ReportRequestDto reportRequestDto, Long reporterId, Long reportedId) {
        this.level = reportRequestDto.getLevel();
        this.reporterId = reporterId;
        this.reportedId = reportedId;
        this.content = reportRequestDto.getContent();
    }
}
