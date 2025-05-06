package ru.renattele.admin95.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BackupDto {
    private Long id;
    private String name;
    private Long size;
    private Boolean running;
    private LocalDateTime timestamp;
    private String message;

    public String sizeFormatted() {
        return FileUtils.byteCountToDisplaySize(size != null ? size : 0);
    }
}
