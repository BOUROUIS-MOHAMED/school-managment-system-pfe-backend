package com.saif.pfe.models;

import com.saif.pfe.models.embeddedId.TeacherClassroomId;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
public class TeacherClassroom extends Base implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private TeacherClassroomId id;

    @ManyToOne
    @MapsId("teacherId")
    private Teacher teacher;

    @ManyToOne
    @MapsId("classroomId")
    private Classroom classroom;

    @Column(name = "disabled")
    private Boolean disabled;
}
