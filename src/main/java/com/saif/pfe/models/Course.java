package com.saif.pfe.models;

import com.saif.pfe.models.ennum.NoteType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Course extends Base{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    private int coefficient;
    private int coefficientDsPercent;
    private int coefficientExamPercent;
    private int coefficientTpPercent;

    @ElementCollection
    @Enumerated(EnumType.STRING) // Store as string values in database
    @CollectionTable(name = "course_note_types", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "note_type")
    private List<NoteType> availableNoteTypes;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
