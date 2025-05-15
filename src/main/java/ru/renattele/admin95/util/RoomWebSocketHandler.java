package ru.renattele.admin95.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public abstract class RoomWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, List<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    public abstract String extractRoomId(WebSocketSession session);

    public void sendMessageToRoom(String roomId, String message) {
        broadcastToRoom(roomId, new TextMessage(message));
    }

    public boolean isRoomExists(String roomId) {
        return rooms.containsKey(roomId);
    }

    public Set<String> getRooms() {
        return rooms.keySet();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = extractRoomId(session);

        if (roomId != null) {
            rooms.computeIfAbsent(roomId, k -> new ArrayList<>())
                    .add(session);

            log.debug("Client connected to room: {}, Session ID: {}", roomId, session.getId());
        } else {
            session.close(CloseStatus.BAD_DATA.withReason("Room ID not provided"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = extractRoomId(session);

        if (roomId != null && rooms.containsKey(roomId)) {
            rooms.get(roomId).remove(session);

            if (rooms.get(roomId).isEmpty()) {
                rooms.remove(roomId);
            }

            log.debug("Client disconnected from room: {}, Session ID: {}", roomId, session.getId());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("Transport error: {}", exception.getMessage());
    }


    private void broadcastToRoom(String roomId, TextMessage message) {
        if (rooms.containsKey(roomId)) {
            List<WebSocketSession> sessions = rooms.get(roomId);
            sessions.forEach(session -> {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(message);
                    }
                } catch (IOException e) {
                    log.debug("Error sending message: {}", e.getMessage());
                }
            });
        }
    }
}
