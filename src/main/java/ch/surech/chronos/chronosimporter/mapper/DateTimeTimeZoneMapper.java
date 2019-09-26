package ch.surech.chronos.chronosimporter.mapper;

import com.microsoft.graph.models.extensions.DateTimeTimeZone;

import java.time.LocalDateTime;

public class DateTimeTimeZoneMapper {

    public static LocalDateTime toLocalDateTime(DateTimeTimeZone dateTimeTimeZone) {
        LocalDateTime result = LocalDateTime.parse(dateTimeTimeZone.dateTime);
        return null;
    }
}
