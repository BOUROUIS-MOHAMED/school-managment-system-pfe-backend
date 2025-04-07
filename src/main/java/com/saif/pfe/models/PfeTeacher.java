package com.saif.pfe.models;

import com.saif.pfe.models.embeddedId.PfeTeacherId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class PfeTeacher extends Base implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private PfeTeacherId id;

    @ManyToOne
    @MapsId("pfeId")
    private Pfe pfe;

    @ManyToOne
    @MapsId("teacherId")
    private Teacher teacher;


}
