package com.saif.pfe.models.embeddedId;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Embeddable
@EqualsAndHashCode
public class CourseStudentId {

    @Column(name = "course_id", insertable = false, updatable = false)
    private Long courseId;

    @Column(name = "student_id", insertable = false, updatable = false)
    private Long studentId;
}
