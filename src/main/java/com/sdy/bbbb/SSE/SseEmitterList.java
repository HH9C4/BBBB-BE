package com.sdy.bbbb.SSE;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Slf4j
public class SseEmitterList {

    // 주의할 점은 이 콜백이 SseEmitter를 관리하는 다른 스레드에서 실행된다는 것입니다.
    // 따라서 thread-safe한 자료구조를 사용하지 않으면 ConcurrnetModificationException이 발생할 수 있습니다.
    // 여기서는 thread-safe한 자료구조인 CopyOnWriteArrayList를 사용하였습니다.
    private final List<SseEmitter> emitterList = new CopyOnWriteArrayList<>();

    private static final AtomicLong counter = new AtomicLong();

    SseEmitter add(SseEmitter emitter) {
        //리스트에 추가
        this.emitterList.add(emitter);
        log.info("new emitter added: {}", emitter);
        log.info("emitter list size: {}", emitterList.size());
        //비동기 요청 완료시나 타임아웃 시 콜백을 등록 할 수 있다.
        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            this.emitterList.remove(emitter);    // 만료되면 리스트에서 삭제
        });
        //타임아웃 발생 시 새로운 Emmitter를 생성하기 때문에 자기 자신을 지워주여야 한다.
        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
        });

        return emitter;
    }

    //카운트 예시
    public void count() {
        long count = counter.incrementAndGet();
        emitterList.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        //어떠한 이름으로?
                        .name("count")
                        //프론트에게 줄 데이터
                        .data(count));

            } catch (IOException e) {
                //커스텀으로 바꾸자
                throw new RuntimeException(e);
            }
        });
    }

}
