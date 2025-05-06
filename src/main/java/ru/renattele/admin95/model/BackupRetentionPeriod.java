package ru.renattele.admin95.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Getter
public enum BackupRetentionPeriod {
    ANY_TIME(0L, ChronoUnit.MILLIS, "backup.retention.any"),
    ONE_HOUR(1L, ChronoUnit.HOURS, "backup.retention.one_hour"),
    ONE_DAY(1L, ChronoUnit.DAYS, "backup.retention.one_day"),
    ONE_WEEK(7L, ChronoUnit.DAYS, "backup.retention.one_week"),
    TWO_WEEKS(14L, ChronoUnit.DAYS, "backup.retention.two_weeks"),
    ONE_MONTH(1L, ChronoUnit.MONTHS, "backup.retention.one_month"),
    THREE_MONTHS(3L, ChronoUnit.MONTHS, "backup.retention.three_months"),
    SIX_MONTHS(6L, ChronoUnit.MONTHS, "backup.retention.six_months"),
    ONE_YEAR(1L, ChronoUnit.YEARS, "backup.retention.one_year");

    private final Long ageAmount;
    private final ChronoUnit chronoUnit;
    private final String messageKey;
}
