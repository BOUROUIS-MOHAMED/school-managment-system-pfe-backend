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
public class Semester extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String year;

    private int semester;

}
