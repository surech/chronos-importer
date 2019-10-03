package ch.surech.chronos.chronosimporter.mapper;

import com.microsoft.graph.models.extensions.DateTimeTimeZone;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeTimeZoneMapper {

    public static ZonedDateTime toZonedDateTime(DateTimeTimeZone dateTimeTimeZone) {
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeTimeZone.dateTime);
        ZonedDateTime result = ZonedDateTime.of(dateTime, ZoneId.of(dateTimeTimeZone.timeZone));
        return result;
    }
}
