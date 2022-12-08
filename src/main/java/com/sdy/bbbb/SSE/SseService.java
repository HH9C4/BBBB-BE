//package com.sdy.bbbb.SSE;
//
//import com.sdy.bbbb.entity.Account;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.io.IOException;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class SseService {
//
//    private static final Long DEFAULT_TIMEOUT = 60 * 1000L;
//
//    private final EmitterRepository emitterRepository;
//
//    private final NotificationRepository notificationRepository;
//
//    public SseEmitter subscribe(Long userId, String lastEventId) {
//
//        // 1 accountId 를 바탕으로 id 생성
//        String id = userId + "_" + System.currentTimeMillis();
//
//        // 2 새로운 SseEmitter 생성 (클라이언트의 sse연결 요청에 대한 응답으로는 SseEmiter 객체를 반환해주어야한다)
//        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));
//
//        //SseEmitter를 생성할 때는 비동기 요청이 완료되거나 타임아웃 발생 시 실행할 콜백을 등록할 수 있습니다.
//        // 타임아웃이 발생하면 브라우저에서 재연결 요청을 보내는데,
//        // 이때 새로운 Emitter 객체를 다시 생성하기 때문에(SseController의 connect()메서드 참조) 기존의 Emitter를 제거해주어야 합니다.
//        // 따라서 onCompletion 콜백에서 자기 자신을 지우도록 등록합니다.
//
//        // 시간이 만료되었거나 어떠한 이유든 비동기 요청이 완료되면 (아무튼 더이상 사용할 수 없음)
//        emitter.onCompletion(() -> emitterRepository.deleteById(id));
//        //비동기 요청시간 초과시 (아무튼 더이상 사용할 수 없음)
//        emitter.onTimeout(() -> emitterRepository.deleteById(id));
//
//        // 3
//        // 503 에러를 방지하기 위한 더미 이벤트 전송
//        sendToClient(emitter, id, "EventStream Created. [userId=" + userId + "]");
//
//        // 4
//        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
//        if (!lastEventId.isEmpty()) {
//            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(String.valueOf(userId));
//            events.entrySet().stream()
//                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
//                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
//        }
//
//        return emitter;
//    }
//
//
//    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
//        try {
//            emitter.send(SseEmitter.event()
//                    .id(eventId)
//                    .data(data));
//        } catch (IOException exception) {
//            emitterRepository.deleteById(emitterId);
//        }
//    }
//    public void send(Account receiver, AlarmType notificationType, String content, String url) {
//        Notification notification = notificationRepository.save(createNotification(receiver, notificationType, content, url));
//
//        String receiverId = String.valueOf(receiver.getId());
//        String eventId = receiverId + "_" + System.currentTimeMillis();
//        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);
//        emitters.forEach(
//                (key, emitter) -> {
//                    emitterRepository.saveEventCache(key, notification);
//                    sendNotification(emitter, eventId, key, NotificationResponseDto.create(notification));
//                }
//        );
//    }
//
//    private Notification createNotification(Account receiver, AlarmType notificationType, String content, String url) {
//        return Notification.builder()
//                .receiver(receiver)
//                .alarmType(notificationType)
//                .content(content)
//                .url(url)
//                .isRead(false)
//                .build();
//    }
//
//
//
//}
