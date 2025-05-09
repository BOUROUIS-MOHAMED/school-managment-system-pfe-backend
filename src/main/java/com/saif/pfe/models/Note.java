package com.saif.pfe.models;

import com.saif.pfe.models.ennum.NoteType;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Note extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float score;

    @Enumerated(EnumType.STRING)
    private NoteType type;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Semester semester;

    @ManyToOne
    private Teacher teacher;

}
