package com.saif.pfe.models;

import com.saif.pfe.models.embeddedId.TeacherCourseId;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class TeacherCourse extends Base{

    @EmbeddedId
    private TeacherCourseId id;

    @ManyToOne
    @MapsId("teacherId")
    private Teacher teacher;

    @ManyToOne
    @MapsId("courseId")
    private Course course;
}
