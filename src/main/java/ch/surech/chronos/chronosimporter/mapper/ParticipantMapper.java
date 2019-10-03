package ch.surech.chronos.chronosimporter.mapper;

import ch.surech.chronos.chronosimporter.model.Participant;
import com.microsoft.graph.models.extensions.Attendee;
import com.microsoft.graph.models.extensions.Recipient;

public class ParticipantMapper {

    private final static String EMPTY_NAME = "<Unknown>";
    private final static String EMPTY_ADRESS = "unknown@example.com";

    public static Participant toParticipant(Recipient recipient) {
        Participant.ParticipantBuilder builder = Participant.builder();
        extractEMail(recipient, builder);

        return builder.build();
    }

    public static Participant toParticipant(Attendee attendee) {
        Participant.ParticipantBuilder builder = Participant.builder();

        extractEMail(attendee, builder);
        builder.attendeeType(attendee.type);
        builder.responseStatus(attendee.status);
        return builder.build();
    }

    private static void extractEMail(Recipient recipient, Participant.ParticipantBuilder builder) {

        if (recipient.emailAddress != null) {
            builder.name(recipient.emailAddress.name);
            builder.address(recipient.emailAddress.address);
        } else {
            builder.name(EMPTY_NAME);
            builder.address(EMPTY_ADRESS);
        }
    }
}
