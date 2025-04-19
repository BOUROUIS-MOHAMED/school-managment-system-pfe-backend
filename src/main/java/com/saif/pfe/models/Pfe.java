package com.saif.pfe.models;

import com.saif.pfe.models.ennum.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Pfe extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @OneToOne
    private Student studentOne;
    @OneToOne
    private Student studentTwo;
    @ManyToOne
    private Teacher supervisor;
    @ManyToOne
    private Teacher president;
    @ManyToOne
    private Teacher rapporteur;

    private String guest;

    LocalDate date;

    private double noteStudentOne;
    private double noteStudentTwo;

    private String linkReport;
    private String linkPresentation;
    private String linkCertificate;
    String information;
    @Enumerated(EnumType.STRING)
    private Status status;
}
