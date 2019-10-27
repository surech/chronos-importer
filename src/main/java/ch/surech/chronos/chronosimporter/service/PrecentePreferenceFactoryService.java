package ch.surech.chronos.chronosimporter.service;

import ch.surech.chronos.api.model.PrecentePreferenceType;
import ch.surech.chronos.api.model.UserPrecentePreference;
import ch.surech.chronos.chronosimporter.model.PrecentePreferenceStereotype;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@Service
public class PrecentePreferenceFactoryService {

    public List<UserPrecentePreference> buildRandomPrecence(){
        PrecentePreferenceStereotype[] values = PrecentePreferenceStereotype.values();
        int rnd = ThreadLocalRandom.current().nextInt(values.length);
        return buildPrecente(values[rnd]);
    }

    public List<UserPrecentePreference> buildPrecente(PrecentePreferenceStereotype stereotype) {
        List<UserPrecentePreference> list = new ArrayList<>();
        switch (stereotype) {
            case SMART_WORKER:
                list.add(createPreference(LocalTime.of(0, 0), LocalTime.of(8, 14), PrecentePreferenceType.NoWork));
                list.add(createPreference(LocalTime.of(8, 15), LocalTime.of(11, 44), PrecentePreferenceType.Available));
                list.add(createPreference(LocalTime.of(11, 45), LocalTime.of(12, 44), PrecentePreferenceType.RatherNot));
                list.add(createPreference(LocalTime.of(12, 45), LocalTime.of(15, 44), PrecentePreferenceType.Available));
                list.add(createPreference(LocalTime.of(15, 45), LocalTime.of(16, 49), PrecentePreferenceType.RatherNot));
                list.add(createPreference(LocalTime.of(17, 00), LocalTime.of(23, 59), PrecentePreferenceType.NoWork));
                return list;
            case LONG_WORKER:
                list.add(createPreference(LocalTime.of(0, 0), LocalTime.of(6, 59), PrecentePreferenceType.NoWork));
                list.add(createPreference(LocalTime.of(7, 0), LocalTime.of(11, 59), PrecentePreferenceType.Available));
                list.add(createPreference(LocalTime.of(12, 0), LocalTime.of(12, 59), PrecentePreferenceType.RatherNot));
                list.add(createPreference(LocalTime.of(13, 0), LocalTime.of(16, 59), PrecentePreferenceType.Available));
                list.add(createPreference(LocalTime.of(17, 00), LocalTime.of(23, 59), PrecentePreferenceType.NoWork));
                return list;
            case MORNING_WORKER:
                list.add(createPreference(LocalTime.of(0, 0), LocalTime.of(7, 59), PrecentePreferenceType.NoWork));
                list.add(createPreference(LocalTime.of(8, 0), LocalTime.of(11, 59), PrecentePreferenceType.Preferred));
                list.add(createPreference(LocalTime.of(12, 0), LocalTime.of(12, 59), PrecentePreferenceType.RatherNot));
                list.add(createPreference(LocalTime.of(13, 0), LocalTime.of(16, 59), PrecentePreferenceType.Available));
                list.add(createPreference(LocalTime.of(17, 00), LocalTime.of(23, 59), PrecentePreferenceType.NoWork));
                return list;
            case AFTERNOON_WORKER:
                list.add(createPreference(LocalTime.of(0, 0), LocalTime.of(7, 59), PrecentePreferenceType.NoWork));
                list.add(createPreference(LocalTime.of(8, 0), LocalTime.of(11, 59), PrecentePreferenceType.Available));
                list.add(createPreference(LocalTime.of(12, 0), LocalTime.of(12, 59), PrecentePreferenceType.RatherNot));
                list.add(createPreference(LocalTime.of(13, 0), LocalTime.of(16, 59), PrecentePreferenceType.Preferred));
                list.add(createPreference(LocalTime.of(17, 00), LocalTime.of(23, 59), PrecentePreferenceType.NoWork));
                return list;
            default:
                throw new IllegalArgumentException("Unknown stereotype: " + stereotype);
        }
    }

    private UserPrecentePreference createPreference(LocalTime from, LocalTime to, PrecentePreferenceType type) {
        UserPrecentePreference.UserPrecentePreferenceBuilder builder = UserPrecentePreference.builder();
        builder.from(from);
        builder.to(to);
        builder.preference(type);
        return builder.build();
    }
}
