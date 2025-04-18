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
    private Student student_one;
    @OneToOne
    private Student student_two;
    @ManyToOne
    private Teacher supervisor;
    @ManyToOne
    private Teacher president;
    @ManyToOne
    private Teacher rapporteur;

    private String guest;

    LocalDate date;

    private double note_student_one;
    private double note_student_two;

    private String link_report;
    private String link_presentation;
    private String link_certificate;
    String information;
    @Enumerated(EnumType.STRING)
    private Status status;
}
