package ru.javaprojects.mylunch.common.util;

import jakarta.annotation.Nonnull;
import lombok.experimental.UtilityClass;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@UtilityClass
public class ClockHolder {

    private static final AtomicReference<Clock> CLOCK_REFERENCE = new AtomicReference<>(Clock.systemDefaultZone());

    @Nonnull
    public static LocalDateTime getDateTime() {
        return LocalDateTime.now(getClock());
    }

    @Nonnull
    public static LocalDate getDate() {
        return LocalDate.now(getClock());
    }

    @Nonnull
    public static LocalTime getTime() {
        return LocalTime.now(getClock());
    }

    @Nonnull
    public static Clock getClock() {
        return CLOCK_REFERENCE.get();
    }

    /**
     * Atomically sets the value to {@code newClock} and returns the old value.
     *
     * @param newClock the new value
     * @return the previous value of clock
     */
    @Nonnull
    public static Clock setClock(@Nonnull final Clock newClock) {
        Objects.requireNonNull(newClock, "newClock cannot be null");
        return CLOCK_REFERENCE.getAndSet(newClock);
    }
}
