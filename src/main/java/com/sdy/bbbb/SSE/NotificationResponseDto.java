package com.sdy.bbbb.SSE;

import com.sdy.bbbb.util.Chrono;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class NotificationResponseDto {

    private Long notificationId;

    private String message;

    private String articlesId;

    private Boolean readStatus;

    private AlarmType alarmType;

    private String title;

    private String createdAt;


    @Builder
    public NotificationResponseDto(Long id, String message, String articlesId, Boolean readStatus,
                                   AlarmType alarmType, String title,String createdAt) {
        this.notificationId = id;
        this.message = message;
        this.articlesId = articlesId;
        this.readStatus = readStatus;
        this.title = title;
        this.alarmType = alarmType;
        this.createdAt = createdAt;
    }

    public NotificationResponseDto(Notification notification) {
        this.notificationId = notification.getId();
        this.message = notification.getMessage();
        this.articlesId = notification.getUrl();
        this.readStatus = notification.getIsRead();
        this.title = notification.getTitle();
        this.alarmType = notification.getAlarmType();
        this.createdAt = Chrono.timesAgo(notification.getCreatedAt());
    }

    public static NotificationResponseDto create(Notification notification) {
        String createdAt = Chrono.timesAgo(notification.getCreatedAt());

        return NotificationResponseDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .alarmType(notification.getAlarmType())
                .articlesId(notification.getUrl())
                .title(notification.getTitle())
                .readStatus(notification.getIsRead())
                .createdAt(createdAt)
                .build();
    }
}
