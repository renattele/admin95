package ru.renattele.admin95.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import ru.renattele.admin95.model.BackupRetentionPeriod;
import ru.renattele.admin95.model.FileSizeUnit;
import ru.renattele.admin95.validation.EnumValidator;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BackupDeleteQueryForm {
    @EnumValidator(enumClazz = BackupRetentionPeriod.class)
    private String age;

    @NumberFormat
    private Long minSize;

    @EnumValidator(enumClazz = FileSizeUnit.class)
    private String minSizeUnit;

    @NumberFormat
    private Long maxSize;

    @EnumValidator(enumClazz = FileSizeUnit.class)
    private String maxSizeUnit;
}
