package ru.renattele.admin95.model;

public enum TaskRunPeriod {
    EVERY_DAY("Every day"),
    EVERY_WEEK("Every week"),
    EVERY_MONTH("Every month");

    public final String displayName;

    TaskRunPeriod(String displayName) {
        this.displayName = displayName;
    }
}
