package com.saif.pfe.models;

import com.saif.pfe.models.embeddedId.CourseStudentId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity

public class CourseStudent extends Base {

    @EmbeddedId
    private CourseStudentId id;

    @ManyToOne
    @MapsId("courseId")
    private Course course;

    @ManyToOne
    @MapsId("studentId")
    private Student student;
}
