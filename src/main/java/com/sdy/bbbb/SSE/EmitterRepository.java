package com.sdy.bbbb.SSE;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.yaml.snakeyaml.emitter.Emitter;

import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    void saveEventCache(String emitterId, Object event);
    Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId);
    Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId);
    void deleteById(String id);
    void deleteAllEmitterStartWithId(String memberId);
    void deleteAllEventCacheStartWithId(String memberId);

    Map<String, Object> findAllEventCacheStartWithId(String valueOf);

    Map<String, SseEmitter> findAllStartWithById(String id);
}
