package com.sdy.bbbb.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //클라이언트에서 보낸 메세지를 받을 prefix
        registry.setApplicationDestinationPrefixes("/pub");
        // 해당 주소를 구독하고 있는 클라이언트들에게 메세지 전달
        registry.enableSimpleBroker("/sub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") //SockJS 연결주소
                .setAllowedOriginPatterns("*")
                .withSockJS(); //버전 낮은 브라우저에서도 적용 가능
        // 주소 : ws://localhost:8080/ws-stomp
    }
}
