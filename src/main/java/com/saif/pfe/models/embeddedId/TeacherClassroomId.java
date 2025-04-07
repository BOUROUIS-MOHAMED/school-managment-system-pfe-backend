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
public class TeacherClassroomId {

    @Column(name = "teacher_id", insertable = false, updatable = false)
    private Long teacherId;

    @Column(name = "classroom_id", insertable = false, updatable = false)
    private Long classroomId;
}
