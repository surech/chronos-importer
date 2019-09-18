package ch.surech.chronos.chronosimporter.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Participant")
@Getter @Setter
@NoArgsConstructor
public class Participant {

    private String name;

    private String address;
}
