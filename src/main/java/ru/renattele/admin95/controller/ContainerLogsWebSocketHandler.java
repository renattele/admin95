package ru.renattele.admin95.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import ru.renattele.admin95.service.docker.DockerProjectQueryService;
import ru.renattele.admin95.ws.RoomWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContainerLogsWebSocketHandler extends RoomWebSocketHandler {
    private final DockerProjectQueryService dockerProjectQueryService;

    @Scheduled(fixedRateString = "${docker.refresh-logs-interval}")
    private void refreshLogs() {
        var projects = dockerProjectQueryService.getProjects();
        var rooms = getRooms();
        for (var room : rooms) {
            var project = projects.stream().filter(p -> p.getName().equals(room)).findFirst();
            if (project.isPresent()) {
                var logs = dockerProjectQueryService.logsForProject(project.get());
                sendMessageToRoom(room, logs);
            }
        }
    }

    @Override
    public String extractRoomId(WebSocketSession session) {
        String path = session.getUri().getPath();
        String[] pathSegments = path.split("/");

        if (pathSegments.length >= 6) {
            return pathSegments[5];
        }
        return null;
    }
}
