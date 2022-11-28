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


    public Report(ReportRequestDto reportRequestDto) {
        this.level = reportRequestDto.getLevel();
        this.reporterId = reportRequestDto.getReporterId();
        this.reportedId = reportRequestDto.getReportedId();
        this.content = reportRequestDto.getContent();
    }
}
