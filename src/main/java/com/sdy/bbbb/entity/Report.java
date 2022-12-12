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

    @Column
    private String content;



    public Report(ReportRequestDto reportRequestDto, Long reporterId, Long reportedId) {
        this.level = reportRequestDto.getLevel();
        this.reporterId = reporterId;
        this.reportedId = reportedId;
        this.content = reportRequestDto.getContent();
    }
}
