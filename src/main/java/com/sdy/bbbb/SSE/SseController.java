package com.sdy.bbbb.SSE;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SseController {

    private final SseEmitterList sseEmitterList;
    private final SseService2 sseService;
    //다른 컨트롤러에서 얘를 호출해야하나?


    //로그인 시 (버튼클릭 시) connect로 요청을 하도록(유저마다)
    //
    //구독 요청
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @RequestParam(required = false) String lastEventId) {

        return sseService.subscribe(userDetails.getAccount().getId(), lastEventId);
//        //들어온 요청에 대한 새로운 emitter 생성
//        SseEmitter emitter = new SseEmitter(60 * 1000L);
//        //emitterList에 추가
//        sseEmitterList.add(emitter);
//
//        //첫 연결 시 503 Error 방지를 위한 더미데이터 전달
//        try {
//             emitter.send(SseEmitter.event()
//                    .name("connect")
//                    .data("connected!"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseEntity.ok(emitter);
    }

    //이거를 알림요청 진짜요청 따로할 지
    //아니면 요청시 자동으로 알림이 가게끔 할 지?
    //댓글알림 -> 댓글 작성 api 한번호출 -> 댓글알림 api(sse) 호출
    @PostMapping("/count")
    public ResponseEntity<Void> count() {
        sseEmitterList.count();
        return ResponseEntity.ok().build();
    }

}
