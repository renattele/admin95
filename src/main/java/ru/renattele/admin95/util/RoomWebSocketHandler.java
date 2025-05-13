package ru.renattele.admin95.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public abstract class RoomWebSocketHandler implements WebSocketHandler {
    private final Map<String, Map<String, WebSocketSession>> rooms = new ConcurrentHashMap<>();
    private final Map<String, Sinks.Many<String>> roomSinks = new ConcurrentHashMap<>();

    public abstract String extractRoomId(WebSocketSession session);

    public void sendMessageToRoom(String roomId, String message) {
        if (roomSinks.containsKey(roomId)) {
            roomSinks.get(roomId).tryEmitNext(message);
        }
    }

    public boolean isRoomExists(String roomId) {
        return rooms.containsKey(roomId);
    }

    public Set<String> getRooms() {
        return rooms.keySet();
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String roomId = extractRoomId(session);

        if (roomId == null) {
            log.debug("Room ID not provided for session: {}", session.getId());
            return session.close();
        }

        // Add session to room
        rooms.computeIfAbsent(roomId, k -> new ConcurrentHashMap<>())
                .put(session.getId(), session);

        // Create sink for room if it doesn't exist
        Sinks.Many<String> sink = roomSinks.computeIfAbsent(roomId,
                k -> Sinks.many().multicast().onBackpressureBuffer());

        log.debug("Client connected to room: {}, Session ID: {}", roomId, session.getId());

        // Handle session completion (client disconnect)
        session.closeStatus().subscribe(status -> {
            log.debug("Client disconnected from room: {}, Session ID: {}", roomId, session.getId());
            if (rooms.containsKey(roomId)) {
                rooms.get(roomId).remove(session.getId());
                if (rooms.get(roomId).isEmpty()) {
                    rooms.remove(roomId);
                    roomSinks.remove(roomId);
                }
            }
        });

        // Subscribe to incoming messages
        Mono<Void> input = session.receive()
                .doOnNext(message -> log.debug("Received message in room: {}", roomId))
                .then();

        // Send messages to client
        Mono<Void> output = session.send(
                sink.asFlux()
                        .map(text -> session.textMessage(text)));

        // Combine both streams
        return Mono.zip(input, output).then();
    }
}