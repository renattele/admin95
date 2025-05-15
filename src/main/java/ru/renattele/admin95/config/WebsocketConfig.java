package ru.renattele.admin95.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ru.renattele.admin95.controller.ContainerLogsWebSocketHandler;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebsocketConfig implements WebSocketConfigurer {
    private final ContainerLogsWebSocketHandler containerLogsWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(containerLogsWebSocketHandler, "/ws/admin/containers/logs/{id}")
                .setAllowedOrigins("*");
    }
}